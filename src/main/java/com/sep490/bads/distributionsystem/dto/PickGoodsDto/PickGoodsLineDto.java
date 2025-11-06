package com.sep490.bads.distributionsystem.dto.PickGoodsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PickGoodsLineDto {
    private String sku;
    private String productName;
    private String unitName;
    private long quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
