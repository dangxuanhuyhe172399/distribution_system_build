package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.UserGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDto {
    @NotBlank private String username;
    @NotBlank @Size(min=6) private String password;
    @NotBlank private String fullName;
    @Email
    private String email;
    private String phone;
    private Long roleId;
    private UserGender gender;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate dateOfBirth;
    private String address;
}
