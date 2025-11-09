package com.sep490.bads.distributionsystem.dto.salesOrderDtos;

import com.sep490.bads.distributionsystem.entity.type.OverallProgress;
import com.sep490.bads.distributionsystem.entity.type.ReviewStatus;
import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class OrderProgressDto {
    private Long orderId;
    private String customerName;
    private ReviewStatus financeStatus;
    private ReviewStatus warehouseStatus;
    private String note;
    private OverallProgress overall;
}
