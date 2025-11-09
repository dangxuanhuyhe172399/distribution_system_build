package com.sep490.bads.distributionsystem.dto.salesOrderDtos;

import com.sep490.bads.distributionsystem.entity.type.ReviewStatus;
import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
public class OrderProgressUpdateDto {
    private ReviewStatus financeStatus;
    private ReviewStatus warehouseStatus;
    private String note;
}
