package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.UserDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    UserDto getCurrentUser(Authentication authentication);
}
