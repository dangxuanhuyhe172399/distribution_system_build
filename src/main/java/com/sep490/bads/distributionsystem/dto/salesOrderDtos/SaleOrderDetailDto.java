package com.sep490.bads.distributionsystem.dto.salesOrderDtos;

import com.sep490.bads.distributionsystem.entity.Product;
import com.sep490.bads.distributionsystem.entity.SalesOrder;
import com.sep490.bads.distributionsystem.entity.type.SaleOrderDetailStatus;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleOrderDetailDto {
    private Long id;

    private SalesOrder order;

    private Product product;

    private Long quantity;

    private BigDecimal unitPrice;

    private BigDecimal discount;

    private BigDecimal vatAmount;

    private BigDecimal totalPrice;

    private SaleOrderDetailStatus status;

    private String note;
}
