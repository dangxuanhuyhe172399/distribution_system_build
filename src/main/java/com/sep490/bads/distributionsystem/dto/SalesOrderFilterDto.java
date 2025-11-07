package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderFilterDto {

    private String keyword;       // tìm theo tên KH, mã đơn
    private String status;        // PENDING, CONFIRMED, CANCELED
    private String paymentMethod; // lọc theo hình thức thanh toán

    private String sortBy = "createdAt";
    private String direction = "DESC";

    @Min(0)
    private int page = 0;
    @Min(1)
    private int size = 10;

    @PositiveOrZero
    private Long minTotal;        // lọc khoảng tổng tiền
    @PositiveOrZero
    private Long maxTotal;
}
