package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.stockDtos.CreateIssueDto;
import com.sep490.bads.distributionsystem.dto.stockDtos.CreateReceiptDto;
import com.sep490.bads.distributionsystem.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/public/stock")
@Tag(name = "Stock", description = "Quản lý xuất/nhập kho")
@RequiredArgsConstructor
public class StockController extends BaseController {
    private final StockService stockService;

    @Operation(summary="Tạo & post phiếu nhập")
    @PostMapping("/receipts:post")
    public ResponseEntity<?> createPostReceipt(@RequestBody @Valid CreateReceiptDto dto,
                                               Authentication auth){
        Long uid = getUserDetails(auth).getUserId();
        return ResponseEntity.ok(stockService.createAndPost(dto, uid));
    }

    @Operation(summary="Tạo & post phiếu xuất")
    @PostMapping("/issues:post")
    public ResponseEntity<?> createPostIssue(@RequestBody @Valid CreateIssueDto dto,
                                             Authentication auth){
        Long uid = getUserDetails(auth).getUserId();
        return ResponseEntity.ok(stockService.createAndPost(dto, uid));
    }

    // ===== ISSUE: tách draft → confirm-pick → post =====
    @Operation(summary="Tạo phiếu lấy hàng")
    @PostMapping("/issues")
    public ResponseEntity<?> createIssueDraft(@RequestBody @Valid CreateIssueDto dto, Authentication a){
        return ResponseEntity.ok(stockService.createIssueDraft(dto, getUserDetails(a).getUserId()));
    }
    @Operation(summary="Để người dùng xác nhận từ sale gửi cho người dùng")
    @PatchMapping("/issues/{id}/confirm-pick")
    public ResponseEntity<?> confirmPick(@PathVariable Long id, Authentication a){
        return ResponseEntity.ok(stockService.confirmPick(id, getUserDetails(a).getUserId()));
    }
    @Operation(summary="Xác nhận lấy hàng ")
    @PostMapping("/issues/{id}:post")
    public ResponseEntity<?> postIssue(@PathVariable Long id, Authentication a){
        return ResponseEntity.ok(stockService.postIssue(id, getUserDetails(a).getUserId()));
    }
}
