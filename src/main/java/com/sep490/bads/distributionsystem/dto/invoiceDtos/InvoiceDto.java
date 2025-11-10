package com.sep490.bads.distributionsystem.dto.invoiceDtos;

import com.sep490.bads.distributionsystem.entity.User;
import com.sep490.bads.distributionsystem.entity.type.DeliveryStatus;
import com.sep490.bads.distributionsystem.entity.type.InvoiceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {
    private Long id;
    private String invoiceCode;
    private Long orderId;

    private String customerName;
    private String customerPhone;

    private String supplierName;
    private String paymentMethod;

    private BigDecimal vatAmount;
    private BigDecimal grandTotal;

    private User createdBy;
//    private InvoiceStatus status;
//    private DeliveryStatus deliveryStatus;

}
