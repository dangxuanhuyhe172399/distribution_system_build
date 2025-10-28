package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private UserStatus status;
    private Long roleId;
    private String roleName;
    private String gender;
    private String userCode;
    private Date dateOfBirth;
    private String avatar;
    private String address;
    private Long createdAt;
    private Long updatedAt;
}
