package com.sep490.bads.distributionsystem.dto.purchaseOrderDtos;
import com.sep490.bads.distributionsystem.entity.type.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderListItemDto {
    private Long id;
    private String code;         // PO-2025-001
    private String supplierName;     // Nhà cung cấp
    private String contactPhone;     // Liên hệ (phone NCC)
    private String inChargeName;   // Người tạo
    private String createdAt;    // dd/MM/yyyy
    private BigDecimal totalAmount;  // Tổng tiền hàng
    private ContractStatus status;   // Trạng thái đơn
    private Boolean archived;        // đã lưu trữ hay chưa
}
