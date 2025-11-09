package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.dto.ReturnGoodsDto.ReturnCreateDto;
import com.sep490.bads.distributionsystem.dto.ReturnGoodsDto.ReturnGoodsDetailDto;
import com.sep490.bads.distributionsystem.dto.ReturnGoodsDto.ReturnInspectDto;
import com.sep490.bads.distributionsystem.response.ResultResponse;
import com.sep490.bads.distributionsystem.service.ReturnGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/public/returngoods")
@Tag(name = "Return Goods", description = "quản lý trả hàng ")
@RequiredArgsConstructor
public class ReturnGoodsController extends BaseController{
    @Autowired
    private final ReturnGoodsService service;

    @PostMapping("/requests")
    public ResponseEntity<?> create(@RequestBody @Valid ReturnCreateDto dto, Authentication a){
        return ResponseEntity.ok(service.create(dto, getUserDetails(a).getUserId()));
    }

    @PatchMapping("/{id}/inspect")
    public ResponseEntity<?> inspect(@PathVariable Long id, @RequestBody @Valid ReturnInspectDto dto, Authentication a){
        return ResponseEntity.ok(service.inspect(id, dto, getUserDetails(a).getUserId()));
    }

    @PostMapping("/{id}:receipt")
    public ResponseEntity<?> receipt(@PathVariable Long id, @RequestParam Long warehouseId, Authentication a){
        return ResponseEntity.ok(service.receipt(id, warehouseId, getUserDetails(a).getUserId()));
    }

    @PostMapping("/{id}:scrap")
    public ResponseEntity<?> scrap(@PathVariable Long id, @RequestParam Long warehouseId, Authentication a){
        return ResponseEntity.ok(service.scrap(id, warehouseId, getUserDetails(a).getUserId()));
    }

    @Operation(summary="Chi tiết phiếu trả hàng")
    @GetMapping("/requests/{id}")
    public ResultResponse<ReturnGoodsDetailDto> getGoodsDetail(@PathVariable Long id){
        return ResultResponse.success(service.getReturnDetail(id));
    }
}
