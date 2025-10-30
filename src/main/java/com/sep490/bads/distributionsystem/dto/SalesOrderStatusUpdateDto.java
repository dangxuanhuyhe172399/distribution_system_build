package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SalesOrderStatusUpdateDto {
    @NotBlank private String status; // NEW, CONFIRMED,  CANCELED, ...
}
