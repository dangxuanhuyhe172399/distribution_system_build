package com.sep490.bads.distributionsystem.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CategoryCreateDto {
    @NotBlank
    @NotNull(message = "Tên không được bỏ trống")
    @Size(max=100)
    private String name;
}