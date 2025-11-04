package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsReceiptCreateDto {
    @NotNull private Long warehouseId;
    private Long contractId;
    private String note;
    private List<GoodsReceiptDetailCreateDto> details;
}
