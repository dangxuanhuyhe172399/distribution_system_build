package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.UserGender;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String userCode;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String avatar;

    private UserGender gender;
    private UserStatus status;

    private Long roleId;
    private String roleName;

    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
}
