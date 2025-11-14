package com.sep490.bads.distributionsystem.dto.purchaseOrderDtos;

import com.sep490.bads.distributionsystem.entity.type.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePurchaseOrderDto {
    private Long supplierId;
    private Long inChargeId; // currentUser
    private String note;
    private ContractStatus status;  // DRAFT (Lưu nháp) / PENDING_APPROVAL (Tạo đơn)
    private List<PurchaseOrderItemDto> items;
}
