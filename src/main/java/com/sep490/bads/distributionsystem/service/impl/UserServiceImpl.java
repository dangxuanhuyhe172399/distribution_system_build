package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.config.security.service.UserDetailsImpl;
import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.entity.Role;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import com.sep490.bads.distributionsystem.exception.BadRequestException;
import com.sep490.bads.distributionsystem.exception.NotFoundException;
import com.sep490.bads.distributionsystem.mapper.UserMapper;
import com.sep490.bads.distributionsystem.repository.RoleRepository;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import com.sep490.bads.distributionsystem.service.UserService;
import com.sep490.bads.distributionsystem.utils.Constants;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    private String genUserCode() {
        return "DH" + String.format("%03d", ThreadLocalRandom.current().nextInt(1, 1000));
    }

    public User findByUsername(String username) {
        return userRepository.findActiveByUsername(username).orElse(null);
    }
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    public boolean existsByEmail(String email) {
        return userRepository.findAll().stream().anyMatch(u -> u.getEmail() != null && u.getEmail().equals(email));
    }

    @Override
    public Page<UserDto> search(Pageable pageable, UserFilterDto f) {
        return userRepository.findAll(buildSpec(f, true), pageable)
                .map(this::toDtoWithRole);
    }
    private Specification<User> buildSpec(UserFilterDto f, boolean includeStatus) {
        return (root, q, cb) -> {
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.notEqual(root.get("status"), UserStatus.DELETED));
            if (includeStatus && f.getStatus()!=null)
                ps.add(cb.equal(root.get("status"), f.getStatus()));
            if (f.getRoleId()!=null)
                ps.add(cb.equal(root.join("role").get("roleId"), f.getRoleId()));
            if (StringUtils.hasText(f.getQ())) {
                String kw = "%" + f.getQ().trim().toLowerCase() + "%";
                ps.add(cb.or(
                        cb.like(cb.lower(root.get("userCode")), kw),
                        cb.like(cb.lower(root.get("username")), kw),
                        cb.like(cb.lower(root.get("fullName")), kw),
                        cb.like(cb.lower(root.get("email")), kw)
                ));
            }
            if (f.getFrom()!=null)
                ps.add(cb.greaterThanOrEqualTo(root.get("createdAt"), f.getFrom().atStartOfDay()));
            if (f.getTo()!=null)
                ps.add(cb.lessThan(root.get("createdAt"), f.getTo().plusDays(1).atStartOfDay()));
            return cb.and(ps.toArray(new Predicate[0]));
        };
    }


    @Override
    public UserDto findDtoById(Long id) {
        return toDtoWithRole(findByIdAlive(id));
    }

    private User findByIdAlive(Long id) {
        return userRepository.findOne(buildUserSpecification(id))
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
    }

    public User findByIdWithRole(Long id) {
        return userRepository.findByIdFetchRole(id).orElse(null);
    }

    private UserDto toDtoWithRole(User u) {
        var dto = userMapper.toDto(u);
        if (u.getRole() != null) {
            dto.setRoleId(u.getRole().getRoleId());
            dto.setRoleName(u.getRole().getRoleName());
        }
        return dto;
    }

    public User findById(Long id) {
        return userRepository.findOne(buildUserSpecification(id)).orElseThrow(() ->
                new NotFoundException(Constants.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public UserDto createUser(UserCreateDto dto) {
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new BadRequestException("Username existed");
        if (StringUtils.hasText(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail()))
            throw new BadRequestException("Email existed");
        if (dto.getRoleId() == null) throw new BadRequestException("roleId is required");

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new NotFoundException("Role not found"));

        User u = new User();
        u.setUsername(dto.getUsername());
        u.setPassword(passwordEncoder != null ? passwordEncoder.encode(dto.getPassword()) : dto.getPassword());
        u.setFullName(dto.getFullName());
        u.setEmail(dto.getEmail());
        u.setPhone(dto.getPhone());
        u.setGender(dto.getGender() != null ? dto.getGender().name() : null);
        u.setDateOfBirth(dto.getDateOfBirth());
        u.setAddress(dto.getAddress());
        u.setStatus(UserStatus.PENDING);
        u.setRole(role);
        u.setUserCode(genUserCode());

        return toDtoWithRole(userRepository.save(u));
    }

    @Override
    @Transactional
    public void updateUser(Long id, UserUpdateDto updateData) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));

        if (updateData.getEmail() != null && !updateData.getEmail().equals(u.getEmail())
                && userRepository.existsByEmail(updateData.getEmail()))
            throw new BadRequestException("Email existed");

        if (updateData.getFullName() != null) u.setFullName(updateData.getFullName());
        if (updateData.getEmail() != null) u.setEmail(updateData.getEmail());
        if (updateData.getPhone() != null) u.setPhone(updateData.getPhone());
        if (updateData.getGender() != null) u.setGender(updateData.getGender().name());
        if (updateData.getDateOfBirth() != null) u.setDateOfBirth(updateData.getDateOfBirth());
        if (updateData.getAddress() != null) u.setAddress(updateData.getAddress());
        if (updateData.getAvatar() != null) u.setAvatar(updateData.getAvatar());
        if (updateData.getRoleId() != null) {
            Role r = roleRepository.findById(updateData.getRoleId())
                    .orElseThrow(() -> new NotFoundException("Role not found"));
            u.setRole(r);
        }
        userRepository.save(u);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long id, UserStatus status) {
        if (status == UserStatus.DELETED) throw new BadRequestException("Use DELETE /{id} for soft delete");
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
        u.setStatus(status);
        userRepository.save(u);
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        var spec = (Specification<User>) (root, q, cb) ->
                cb.notEqual(root.get("status"), UserStatus.DELETED);
        return userRepository.findAll(spec, pageable).map(this::toDtoWithRole);
    }

    @Override
    @Transactional
    public void softDeleteUser(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
        u.setStatus(UserStatus.DELETED);
        userRepository.save(u);
    }

    @Override
    public UserDto getProfile(Long userId) {
        return toDtoWithRole(findByIdAlive(userId));
    }

    @Override
    public void updateUserProfile(UserDetailsImpl me, UserProfileUpdateDto dto) {
        User u = userRepository.findById(me.getUserId())
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
        if (dto.getDateOfBirth() != null) u.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getGender() != null)      u.setGender(dto.getGender().name());
        if (dto.getAddress() != null)     u.setAddress(dto.getAddress());
        if (dto.getPhone() != null)       u.setPhone(dto.getPhone());
        if (StringUtils.hasText(dto.getAvatarUrl())) u.setAvatar(dto.getAvatarUrl());
        userRepository.save(u);
    }

    @Override
    public void updateUserAvatar(UserDetailsImpl me, MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new BadRequestException("file is required");
        // upload to R2, get URL, update user.avatar
        throw new BadRequestException("Upload ảnh chưa được cấu hình");
    }
    public User save(User user) {
        return userRepository.save(user);
    }

    //Export
    @Override
    @Transactional(readOnly = true)
    public ByteArrayResource exportFile(UserFilterDto f) {
        List<User> rows = userRepository.findAll(
                buildSpec(f, true),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        StringBuilder sb = new StringBuilder("UserCode,FullName,Email,Role,Status,CreatedAt\n");
        for (User u : rows) {
            sb.append(csv(u.getUserCode())).append(',')
                    .append(csv(u.getFullName())).append(',')
                    .append(csv(u.getEmail())).append(',')
                    .append(csv(u.getRole()!=null?u.getRole().getRoleName():null)).append(',')
                    .append(csv(u.getStatus()!=null?u.getStatus().name():null)).append(',')
                    .append(csv(u.getCreatedAt()!=null?u.getCreatedAt().toString():null))
                    .append('\n');
        }
        return new ByteArrayResource(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String csv(String v) {
        if (v == null) return "";
        String s = v.replace("\"", "\"\"");
        return "\"" + s + "\"";          // chuẩn RFC4180
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (passwordEncoder == null)
            throw new BadRequestException("Password encoder not configured");

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }



    public static Specification<User> buildUserSpecification(Long id) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (id != null) predicates.add(cb.equal(root.get("id"), id));
            else predicates.add(cb.notEqual(root.get("status"), UserStatus.DELETED));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
