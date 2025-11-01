//package com.sep490.bads.distributionsystem.controller;
//
//import com.sep490.bads.distributionsystem.security.service.UserDetailsImpl;
//import com.sep490.bads.distributionsystem.service.InventoryService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import software.amazon.awssdk.services.s3.model.InventoryFilter;
//
//@RestController
//@RequestMapping("v1/public/inventory")
//@RequiredArgsConstructor
//@Validated
//public class InventoryController extends BaseController{
//    private final InventoryService inventoryService;
//    private final ImportService importService;
//    private final ExportService exportService;
//    private final StockTakeService stockTakeService;
//    private final AdjustmentService adjustmentService;
//
//    @Operation(summary="Danh sách tồn kho")
//    @GetMapping
//    public ResponseEntity<?> ListInventory(
//            @RequestParam(required=false) Long warehouseId,
//            @RequestParam(required=false) String q,
//            @RequestParam(required=false) String status,
//            @PageableDefault(size=10, sort="productId") Pageable pageable) {
//        var page = inventoryService.list(new InventoryFilter(warehouseId, q, status), pageable);
//        return ResponseEntity.ok(page);
//    }
//
//    @Operation(summary="Lịch sử giao dịch theo sản phẩm-kho")
//    @GetMapping("/{warehouseId}/{productId}/history")
//    public ResponseEntity<?> history(@PathVariable Long warehouseId, @PathVariable Long productId){
//        return ResponseEntity.ok(inventoryService.history(productId, warehouseId));
//    }
//
//    @Operation(summary="Nhập kho: tạo + ")
//    @PostMapping("/imports")
//    public ResponseEntity<?> importPost(@RequestBody CreateImportNoteDto dto,
//                                        Authentication auth){
//        Long userId = ((UserDetailsImpl)auth.getPrincipal()).getId();
//        return ResponseEntity.ok(importService.createAndPost(dto, userId));
//    }
//
//    @Operation(summary="Xuất kho: tạo +")
//    @PostMapping("/exports")
//    public ResponseEntity<?> exportPost(@RequestBody CreateExportNoteDto dto,
//                                        Authentication auth){
//        Long userId = ((UserDetailsImpl)auth.getPrincipal()).getId();
//        return ResponseEntity.ok(exportService.createAndPost(dto, userId));
//    }
//
//    @Operation(summary="Kiểm kê: chênh lệch")
//    @PostMapping("/stock-takes")
//    public ResponseEntity<?> stockTake(@RequestBody CreateStockTakeDto dto,
//                                       Authentication auth){
//        Long userId = ((UserDetailsImpl)auth.getPrincipal()).getId();
//        stockTakeService.post(dto, userId);
//        return ResponseEntity.ok("OK");
//    }
//
//    @Operation(summary="Điều chỉnh nhanh +/- tồn")
//    @PostMapping("/adjustments")
//    public ResponseEntity<?> adjust(@RequestBody AdjustmentDto dto, Authentication auth){
//        Long userId = ((UserDetailsImpl)auth.getPrincipal()).getId();
//        adjustmentService.adjust(dto, userId);
//        return ResponseEntity.ok("OK");
//    }
//}
