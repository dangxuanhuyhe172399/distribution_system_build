package com.sep490.bads.distributionsystem.dto.salesOrderDtos;

import com.sep490.bads.distributionsystem.entity.type.SaleOderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderDto {
    private Long id;
    private String saleOrderCode;
    private Long customerId;
    private String customerName;
    private Long userId;
    private String userName;
    private String orderDate;
    private SaleOderStatus status;
    private String paymentMethod;
    private String note;
    private BigDecimal subTotal;
    private BigDecimal discountTotal;
    private BigDecimal grandTotal;
    private List<SalesOrderItemDto> items;
}
