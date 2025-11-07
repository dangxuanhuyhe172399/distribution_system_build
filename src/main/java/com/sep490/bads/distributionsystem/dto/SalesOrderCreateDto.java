package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class SalesOrderCreateDto {
    @NotNull
    private Long customerId;
    @NotNull
    private Long userId;         // nhân viên tạo
    @Size(max=50)
    private String paymentMethod;
    @Size(max=255)
    private String note;
    @NotEmpty
    private List<SalesOrderItemCreateDto> items;
}
