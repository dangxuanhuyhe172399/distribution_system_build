package com.sep490.bads.distributionsystem.dto.purchaseOrderDtos;
import com.sep490.bads.distributionsystem.entity.type.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderDetailDto {
    private Long id;

    // Header
    private String code;             // Mã PO
    private String createdAt;        // Ngày tạo đơn (dd/MM/yyyy)

    // Người phụ trách / Người tạo
    private Long inChargeId;
    private String inChargeName;

    // Nhà cung cấp
    private Long supplierId;
    private String supplierName;
    private String supplierAddress;
    private String supplierContactName;
    private String supplierPhone;
    private String supplierTaxCode;

    // Nội dung khác
    private String note;
    private ContractStatus status;
    private Boolean archived;

    // Hàng hóa
    private List<PurchaseOrderItemDto> items;

    // Tổng hợp
    private BigDecimal totalGoodsAmount;   // Tổng tiền hàng (sum lineTotal)
    private BigDecimal totalVatAmount;     // Tổng VAT (sum vatAmount)
    private BigDecimal grandTotal;         // Tổng cộng (totalGoodsAmount + totalVatAmount)
}
