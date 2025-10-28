package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.UserGender;
import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.usertype.UserType;

@Data
public class UserCreateDto {
    private String username;
    private String password;
    private String fullName;
    private UserGender gender;
    private String email;
    @NotNull
    private UserType type;

}
