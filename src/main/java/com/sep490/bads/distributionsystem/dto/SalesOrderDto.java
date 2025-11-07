package com.sep490.bads.distributionsystem.dto;

import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SalesOrderDto {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long userId;
    private String userName;
    private String orderDate;
    private String status;
    private String paymentMethod;
    private String note;
    private Long subTotal;
    private Long discountTotal;
    private Long grandTotal;
    private List<SalesOrderItemDto> items;
}
