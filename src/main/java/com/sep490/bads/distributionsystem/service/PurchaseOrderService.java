package com.sep490.bads.distributionsystem.service;

import com.sep490.bads.distributionsystem.dto.purchaseOrderDtos.*;
import com.sep490.bads.distributionsystem.entity.type.ContractStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseOrderService {
    Page<PurchaseOrderListItemDto> searchPurchaseOrders(PurchaseOrderFilterDto filter, Pageable pageable);

    PurchaseOrderDetailDto createPurchaseOrder(CreatePurchaseOrderDto dto, Long currentUserId);

    PurchaseOrderDetailDto getPurchaseOrderDetail(Long id);

    PurchaseOrderDetailDto updatePurchaseOrder(Long id, UpdatePurchaseOrderDto dto, Long currentUserId);

    void changeStatus(Long id, ContractStatus status);
    void archive(Long id);
    PurchaseOrderDetailDto duplicate(Long id, Long currentUserId);
    void sendToSupplier(Long id);
    byte[] generatePdf(Long id);
}
