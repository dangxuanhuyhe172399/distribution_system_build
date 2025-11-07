package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.UserGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data @NoArgsConstructor
@AllArgsConstructor @Builder
public class UserProfileUpdateDto {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate dateOfBirth;
    private UserGender gender;
    private String address;
    private String phone;
    private String avatarUrl;
}
