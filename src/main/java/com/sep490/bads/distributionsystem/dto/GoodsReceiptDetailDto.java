package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsReceiptDetailDto {
    private Long id;
    private Long productId;
    private String productName;
    private Long quantity;
    private LocalDate manufactureDate;
    private LocalDate expiryDate;
    private CommonStatus status;
    private String note;
}
