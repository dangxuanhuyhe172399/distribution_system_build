package com.sep490.bads.distributionsystem.dto.CustomerDto;

import com.sep490.bads.distributionsystem.entity.type.SaleOderStatus;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOrderSummaryDto {
    private Long   orderId;
    private String orderCode;
    private String orderDate;     // yyyy-MM-dd
    private BigDecimal totalAmount;
    private BigDecimal paidAmount; // hóa đơn / thanh toán
    private SaleOderStatus status;
}
