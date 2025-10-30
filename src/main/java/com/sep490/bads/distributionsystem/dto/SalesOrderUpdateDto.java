package com.sep490.bads.distributionsystem.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dùng để cập nhật thông tin đơn hàng (SalesOrder)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderUpdateDto {

    @Size(max = 50)
    private String status;           // Trạng thái đơn hàng: PENDING, PAID, SHIPPED, COMPLETED, CANCELED, INACTIVE

    @Size(max = 50)
    private String paymentMethod;    // Hình thức thanh toán: Chuyển khoản, Tiền mặt, COD, v.v.

    @Size(max = 255)
    private String note;             // Ghi chú thêm của đơn hàng
}
