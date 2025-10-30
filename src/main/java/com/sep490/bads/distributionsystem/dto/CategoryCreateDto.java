package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CategoryCreateDto {
    @NotBlank
    @NotNull(message = "Tên không được bỏ trống")
    @Size(max=100)
    private String name;
}