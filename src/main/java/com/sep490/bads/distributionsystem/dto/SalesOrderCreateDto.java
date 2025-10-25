package com.sep490.bads.distributionsystem.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.*;

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
