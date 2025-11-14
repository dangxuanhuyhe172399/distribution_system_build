package com.sep490.bads.distributionsystem.dto.salesOrderDtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sep490.bads.distributionsystem.entity.*;
import com.sep490.bads.distributionsystem.entity.type.ReviewStatus;
import com.sep490.bads.distributionsystem.entity.type.SaleOderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;

import java.math.BigDecimal;
import java.util.*;

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
    private Customer customer;
    private User user;
    private BigDecimal totalAmount;
    private User createdBy;
    private List<SalesOrderDetail> orderDetails;
    private Invoice invoice;
    private List<Request> requests;
    private ReviewStatus financeStatus;     // PENDING / APPROVED
    private ReviewStatus warehouseStatus;   // PENDING / APPROVED
    private String progressNote;
    private List<GoodsIssues> goodsIssues;
}
