package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.UserCreateDto;
import com.sep490.bads.distributionsystem.dto.UserDto;
import com.sep490.bads.distributionsystem.dto.UserProfileUpdateDto;
import com.sep490.bads.distributionsystem.dto.UserUpdateDto;
import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.UserGender;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import com.sep490.bads.distributionsystem.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> getAllUser();

    UserDto findDtoById(Long id);

    UserDto createUser(@Valid UserCreateDto dto);

    void updateUser(Long id, @Valid UserUpdateDto updateData);

    void updateUserStatus(Long id, UserStatus status);

    Page<UserDto> getAllUsers(Pageable pageable);

    void softDeleteUser(Long id);

    UserDto getProfile(Long userId);

    void updateUserProfile(UserDetailsImpl userDetails, UserProfileUpdateDto dto);
    void updateUserAvatar(UserDetailsImpl userDetails, MultipartFile file);

}
