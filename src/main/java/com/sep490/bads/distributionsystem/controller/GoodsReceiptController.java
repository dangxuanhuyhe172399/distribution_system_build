package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.*;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.GoodsReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("v1/public/goods-receipt")
@Tag(name = "Goods Receipt", description = "Quản lý phiếu nhập hàng")
@RequiredArgsConstructor
public class GoodsReceiptController extends BaseController {

    private final GoodsReceiptService service;

    @Operation(summary = "Tìm kiếm phiếu nhập hàng")
    @GetMapping
    public ResultResponse<Page<GoodsReceiptDto>> search(
            @ModelAttribute GoodsReceiptFilterDto filter,
            @PageableDefault Pageable pageable) {
        return ResultResponse.success(service.search(pageable, filter));
    }

    @Operation(summary = "Lấy chi tiết phiếu nhập hàng")
    @GetMapping("/{id}")
    public ResultResponse<GoodsReceiptDto> get(@PathVariable Long id) {
        return ResultResponse.success(service.findById(id));
    }

    @Operation(summary = "Tạo phiếu nhập hàng")
    @PostMapping
    public ResponseEntity<GoodsReceiptDto> create(Authentication auth,
                                                  @Valid @RequestBody GoodsReceiptCreateDto dto) {
        var user = getUserDetails(auth);
        return ResponseEntity.ok(service.create(dto, user.getUserId()));
    }

    @Operation(summary = "Duyệt (post) phiếu nhập hàng")
    @PutMapping("/{id}/post")
    public ResponseEntity<?> post(Authentication auth, @PathVariable Long id) {
        var user = getUserDetails(auth);
        service.postReceipt(id, user.getUserId());
        return ResponseEntity.ok().build();
    }
}
