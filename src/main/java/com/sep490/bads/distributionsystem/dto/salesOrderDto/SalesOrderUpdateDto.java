package com.sep490.bads.distributionsystem.dto.salesOrderDto;

import com.sep490.bads.distributionsystem.entity.type.SaleOderStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO dùng để cập nhật thông tin đơn hàng (SalesOrder)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderUpdateDto {
    private Long customerId;
    private Long userId;
    private List<SalesOrderItemUpdateDto> items; // full-replace cho đơn giản
    @Size(max = 50)
    private SaleOderStatus status;           // Trạng thái đơn hàng: PENDING, PAID, SHIPPED, COMPLETED, CANCELED
    @Size(max = 50)
    private String paymentMethod;    // Hình thức thanh toán: Chuyển khoản, Tiền mặt, COD, v.v.
    @Size(max = 255)
    private String note;             // Ghi chú thêm của đơn hàng
}
