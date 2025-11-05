package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import com.sep490.bads.distributionsystem.config.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Page<UserDto> search(Pageable pageable, UserFilterDto filter);

    UserDto findDtoById(Long id);

    UserDto createUser(@Valid UserCreateDto dto);

    void updateUser(Long id, @Valid UserUpdateDto updateData);

    void updateUserStatus(Long id, UserStatus status);

    Page<UserDto> getAllUsers(Pageable pageable);

    void softDeleteUser(Long id);

    UserDto getProfile(Long userId);

    //Export excel
    ByteArrayResource exportFile(UserFilterDto filter);

    void updateUserProfile(UserDetailsImpl userDetails, UserProfileUpdateDto dto);
    void updateUserAvatar(UserDetailsImpl userDetails, MultipartFile file);

    void changePassword(Long userId, String oldPassword, String newPassword);


}
