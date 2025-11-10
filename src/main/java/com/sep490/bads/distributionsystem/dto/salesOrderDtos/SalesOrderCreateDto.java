package com.sep490.bads.distributionsystem.dto.salesOrderDtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class SalesOrderCreateDto {
    @NotNull private Long customerId;
    @NotNull private Long userId;         // NV bán
    @Size(max=50) private String paymentMethod;
    @Size(max=255) private String note;
    @NotEmpty private List<SalesOrderItemCreateDto> items;
}
