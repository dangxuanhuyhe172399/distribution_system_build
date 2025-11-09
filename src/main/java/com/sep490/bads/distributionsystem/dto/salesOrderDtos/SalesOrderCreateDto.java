package com.sep490.bads.distributionsystem.dto.salesOrderDtos;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class SalesOrderCreateDto {
    @NotNull private Long customerId;
    @NotNull private Long userId;         // NV bán
    @Size(max=50) private String paymentMethod;
    @Size(max=255) private String note;
    @NotEmpty private List<SalesOrderItemCreateDto> items;
}
