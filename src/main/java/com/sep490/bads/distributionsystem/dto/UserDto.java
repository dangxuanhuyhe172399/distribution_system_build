package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.UserGender;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;

public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private UserGender gender;
    private String imageUrl;
    private String email;
    private UserStatus status;
    private Long birthday;
}
