package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class RoleUpdateDto {
    @NotBlank
    @Size(max=50)
    private String name;
}
