package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.invoiceDtos.InvoiceDto;
import com.sep490.bads.distributionsystem.dto.invoiceDtos.InvoiceFilterDto;
import com.sep490.bads.distributionsystem.dto.response.ApiResponse;
import com.sep490.bads.distributionsystem.dto.salesOrderDtos.SalesOrderDto;
import com.sep490.bads.distributionsystem.dto.salesOrderDtos.SalesOrderFilterDto;
import com.sep490.bads.distributionsystem.entity.type.SaleOderStatus;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/public/invoice")
@Tag(name = "Invoice", description = "Quản lý hoá đơn")
@RequiredArgsConstructor
public class InvoiceController extends BaseController {
    private final InvoiceService service;

    @Operation(summary = "lấy đơn nháp/báo giá để tạo hóa đơn")
    @GetMapping("/drafts-for-invoice")
    public ResultResponse<Page<SalesOrderDto>> draftsForInvoice(
            @RequestParam(required=false) String keyword,
            Pageable pageable) {
        var f = new SalesOrderFilterDto();
        f.setStatus(SaleOderStatus.NEW); // hoặc NEW/PENDING tùy bạn
        f.setKeyword(keyword);
      //  return ResultResponse.success(service.search(pageable, f));
        return null;
    }

    @Operation(summary = "Tạo hóa đơn từ đơn hàng")
    @PostMapping("/from-order/{orderId}")
    public ResultResponse<InvoiceDto> createFromOrder(
            @PathVariable Long orderId,
            @RequestParam Long supplierId,
            @RequestParam(required = false) String paymentMethod
    ) {
        return ResultResponse.created(service.createFromOrder(orderId, supplierId, paymentMethod));
    }

    @Operation(summary = "Phát hành hóa đơn (tạo PDF)")
    @PostMapping("/{id}/issuePdf")
    public ResultResponse<InvoiceDto> issueInvoice(@PathVariable Long id) {
        return ResultResponse.success(service.issuePdfInvoice(id));
    }

    @Operation(summary = "Gửi lại hóa đơn cho khách")
    @PostMapping("/{id}/resend")
    public ResultResponse<InvoiceDto> resendInvoice(@PathVariable Long id) {
        return ResultResponse.success(service.resend(id));
    }

    @Operation(summary = "Xem chi tiết hóa đơn")
    @GetMapping("/{id}")
    public ResultResponse<InvoiceDto> detailInvoice(@PathVariable Long id) {
        return ResultResponse.success(service.getOne(id));
    }

    @Operation(summary = "Lọc danh sách hóa đơn")
    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<Page<InvoiceDto>>> filterInvoice(@RequestBody InvoiceFilterDto f) {
        return ResponseEntity.ok(ApiResponse.success(service.filter(f)));
    }

}
