package com.sep490.bads.distributionsystem.dto.PickGoodsDto;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
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
