package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsReceiptDto {
    private Long id;
    private String receiptCode;
    private Long contractId;
    private String contractCode;
    private Long warehouseId;
    private String warehouseName;
    private LocalDateTime createdAt;
    private Long createdById;
    private String createdByName;
    private LocalDateTime postedAt;
    private Long postedById;
    private String postedByName;
    private CommonStatus status;
    private String note;
    private List<GoodsReceiptDetailDto> details;
}
