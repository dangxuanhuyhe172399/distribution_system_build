package com.sep490.bads.distributionsystem.dto.ReturnGoodsDtos;

import com.sep490.bads.distributionsystem.entity.type.InspectionResultStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReturnGoodsLineDto {
    private String sku;
    private String productName;
    private long qtyReturn;           // SL trả
    private Long inspectedQty;        // SL kiểm định
    private InspectionResultStatus inspectionResult;  // PASS/FAIL
    private String note;              // ghi chú
}
