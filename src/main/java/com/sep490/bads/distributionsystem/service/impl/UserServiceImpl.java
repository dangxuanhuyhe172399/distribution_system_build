package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.UserCreateDto;
import com.sep490.bads.distributionsystem.dto.UserDto;
import com.sep490.bads.distributionsystem.dto.UserProfileUpdateDto;
import com.sep490.bads.distributionsystem.dto.UserUpdateDto;
import com.sep490.bads.distributionsystem.entity.Role;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.UserGender;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import com.sep490.bads.distributionsystem.exception.BadRequestException;
import com.sep490.bads.distributionsystem.exception.NotFoundException;
import com.sep490.bads.distributionsystem.mapper.UserMapper;
import com.sep490.bads.distributionsystem.repository.RoleRepository;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import com.sep490.bads.distributionsystem.security.service.UserDetailsImpl;
import com.sep490.bads.distributionsystem.service.UserService;
import com.sep490.bads.distributionsystem.utils.Constants;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    public List<UserDto> getAllUser() {
        return List.of();
    }

    @Override
    public UserDto findDtoById(Long id) {
        return toDtoWithRole(findByIdAlive(id));
    }

    private User findByIdAlive(Long id) {
        return userRepository.findOne(buildUserSpecification(id))
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
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

    public static Specification<User> buildUserSpecification(Long id) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<Predicate>();
            if (id != null) predicates.add(cb.equal(root.get("id"), id));
            else predicates.add(cb.notEqual(root.get("status"), UserStatus.DELETED));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
