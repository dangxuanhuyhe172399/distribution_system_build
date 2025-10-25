package com.sep490.bads.distributionsystem.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class RoleUpdateDto {
    @NotBlank
    @Size(max=50)
    private String name;
}
