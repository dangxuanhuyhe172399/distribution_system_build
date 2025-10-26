package com.sep490.bads.distributionsystem.service.impl;

import com.sep490.bads.distributionsystem.dto.UserDto;
import com.sep490.bads.distributionsystem.entity.Role;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.repository.RoleRepository;
import com.sep490.bads.distributionsystem.repository.UserRepository;
import com.sep490.bads.distributionsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + id));
        return convertToDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username đã tồn tại!");
        }

        User user = convertToEntity(userDto);
        return convertToDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user để cập nhật"));

        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhoneNumber());
        user.setStatus(true); // hoặc userDto.getStatusId() == 1

        if (userDto.getRoleId() != null) {
            Role role = roleRepository.findById(userDto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role không tồn tại!"));
            user.setRole(role);
        }

        return convertToDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
        user.setStatus(false); // xóa mềm
        userRepository.save(user);
    }

    @Override
    public UserDto getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user đang đăng nhập"));
        return convertToDto(user);
    }

    //  Entity -> DTO
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhone());
        dto.setStatusId(user.getStatus());
        if (user.getRole() != null) {
            dto.setRoleId(user.getRole().getRoleId());
        }
        return dto;
    }


    private User convertToEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());

        // TODO: mã hóa sau

        user.setPassword("123456");
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhoneNumber());
        user.setStatus(true);

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role không tồn tại!"));
            user.setRole(role);
        }

        return user;
    }
}
