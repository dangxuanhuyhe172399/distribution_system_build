package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.purchaseOrderDtos.*;
import com.sep490.bads.distributionsystem.entity.type.ContractStatus;
import com.sep490.bads.distributionsystem.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("v1/public/purchase-orders")
@Tag(name = "PurchaseOrder", description = "Đơn mua hàng")
@RequiredArgsConstructor
public class PurchaseOrderController extends BaseController {

    private final PurchaseOrderService purchaseOrderService;

    @Operation(summary = "List + filter + paging (màn list PO)")
    @GetMapping
    public ResponseEntity<Page<PurchaseOrderListItemDto>> getPurchaseOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Long createdById,
            @RequestParam(required = false) List<ContractStatus> statuses,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @ParameterObject Pageable pageable
    ) {
        PurchaseOrderFilterDto filter = new PurchaseOrderFilterDto(
                search, supplierId, createdById, statuses, fromDate, toDate
        );
        Page<PurchaseOrderListItemDto> page =
                purchaseOrderService.searchPurchaseOrders(filter, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Tạo đơn mua hàng (popup tạo đơn)")
    @PostMapping
    public ResponseEntity<PurchaseOrderDetailDto> createPurchaseOrder(
            @Valid @RequestBody CreatePurchaseOrderDto dto,
            Authentication authentication
    ) {
        Long currentUserId = getUserDetails(authentication).getUserId();
        PurchaseOrderDetailDto result =
                purchaseOrderService.createPurchaseOrder(dto, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Chi tiết đơn mua hàng")
    @GetMapping("getOrderDetail/{id}")
    public ResponseEntity<PurchaseOrderDetailDto> getPurchaseOrderDetail(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrderDetail(id));
    }

    @Operation(summary = "Cập nhật đơn mua hàng (chỉ cho trạng thái Nháp / Chờ phê duyệt)")
    @PutMapping("update/{id}")
    public ResponseEntity<PurchaseOrderDetailDto> updatePurchaseOrder(@PathVariable Long id, @Valid @RequestBody UpdatePurchaseOrderDto dto, Authentication authentication
    ) {
        Long currentUserId = getUserDetails(authentication).getUserId();
        PurchaseOrderDetailDto result =
                purchaseOrderService.updatePurchaseOrder(id, dto, currentUserId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Đổi trạng thái đơn (Nháp -> Chờ phê duyệt -> Đã phê duyệt -> Hoàn thành / Bị từ chối)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id, @RequestParam ContractStatus status) {
        purchaseOrderService.changeStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "")
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        byte[] pdf = purchaseOrderService.generatePdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=PO-" + id + ".txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(pdf);
    }

    @Operation(summary = "")
    @PostMapping("/{id}/send-to-supplier")
    public ResponseEntity<Void> sendToSupplier(@PathVariable Long id) {
        purchaseOrderService.sendToSupplier(id);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "")
    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archive(@PathVariable Long id) {
        purchaseOrderService.archive(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Tao ban draff new")
    @PostMapping("/{id}/duplicate")
    public ResponseEntity<PurchaseOrderDetailDto> duplicate(@PathVariable Long id, Authentication authentication
    ) {
        Long userId = getUserDetails(authentication).getUserId();
        PurchaseOrderDetailDto result = purchaseOrderService.duplicate(id, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
