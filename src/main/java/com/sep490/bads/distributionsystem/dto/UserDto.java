package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import com.sep490.bads.distributionsystem.entity.type.UserGender;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalDate;

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
