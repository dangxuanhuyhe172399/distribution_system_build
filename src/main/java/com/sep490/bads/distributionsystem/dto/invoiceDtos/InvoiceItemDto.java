package com.sep490.bads.distributionsystem.dto.invoiceDtos;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItemDto {
    private String productName;
    private String unitName;
    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private Integer vatRate;
    private BigDecimal vatAmount;
}
