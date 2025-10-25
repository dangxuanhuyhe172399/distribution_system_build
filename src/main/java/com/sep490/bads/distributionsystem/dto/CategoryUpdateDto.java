package com.sep490.bads.distributionsystem.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CategoryUpdateDto {
    @NotBlank @Size(max=100)
    private String name;
}
