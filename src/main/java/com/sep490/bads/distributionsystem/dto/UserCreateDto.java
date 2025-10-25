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

    public static User toUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setFullName(userCreateDto.getFullName());
      //  user.setGender(userCreateDto.getGender());
        user.setEmail(userCreateDto.getEmail());
        //user.setBirthday(userCreateDto.getBirthday());
       // user.setType(userCreateDto.getType());
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }
}
