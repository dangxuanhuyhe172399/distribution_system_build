package com.sep490.bads.distributionsystem.dto;
import lombok.*;
import jakarta.validation.constraints.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class SalesOrderStatusUpdateDto {
    @NotBlank private String status; // NEW, CONFIRMED,  CANCELED, ...
}
