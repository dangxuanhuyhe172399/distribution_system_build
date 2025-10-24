package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.UserCreateDto;
import com.sep490.bads.distributionsystem.dto.UserDto;
import com.sep490.bads.distributionsystem.dto.UserUpdateDto;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.UserGender;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import com.sep490.bads.distributionsystem.mapper.UserMapper;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import com.sep490.bads.distributionsystem.security.service.UserDetailsImpl;
import com.sep490.bads.distributionsystem.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserMapper userMapper;

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
        List<User> userEntities = new ArrayList<>();
    //    userEntities.add(userRepository.findById().get());
        return userMapper.toDto(userEntities);
    }

    public User findById(Long id) {
        return userRepository.findOne(buildUserSpecification(id)).orElse(null);
    }

    @Override
    public UserDto createUser(UserCreateDto dto) {
        return null;
    }

    @Override
    public void updateUser(Long id, UserUpdateDto updateData) {

    }

    @Override
    public void updateUserStatus(Long id, UserStatus status) {

    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll((root, query, cb) -> cb.notEqual(root.get("status"), UserStatus.DELETED), pageable);
    }

    @Override
    public void softDeleteUser(Long id) {

    }

    @Override
    public Object getProfile(Long userId) {
        return null;
    }

    @Override
    public void updateUserProfile(UserDetailsImpl userDetails, MultipartFile file, String birthday, UserGender gender) {

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
