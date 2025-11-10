package com.sep490.bads.distributionsystem.dto.salesOrderDtos;

import com.sep490.bads.distributionsystem.entity.type.SaleOderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderFilterDto {

    private String keyword;       // tìm theo tên KH, mã đơn
    private SaleOderStatus status;        // PENDING, CONFIRMED, CANCELED
    private String paymentMethod; // lọc theo hình thức thanh toán

    private String sortBy = "createdAt";
    private String direction = "DESC";

    @Min(0)
    private int page = 0;
    @Min(1)
    private int size = 10;

    @PositiveOrZero
    private BigDecimal minTotal;        // lọc khoảng tổng tiền
    @PositiveOrZero
    private BigDecimal maxTotal;
}
