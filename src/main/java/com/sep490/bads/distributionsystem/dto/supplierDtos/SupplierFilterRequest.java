package com.sep490.bads.distributionsystem.dto.supplierDtos;

import com.sep490.bads.distributionsystem.entity.type.SupplierStatus;
import lombok.Data;

import java.util.List;

@Data
public class SupplierFilterRequest {
    private String keyword;              // ô “Tìm kiếm nhà cung cấp…”
    private SupplierStatus status;       // ACTIVE / INACTIVE
    private List<Long> categoryIds;      // Bao bì / Sản phẩm / ...
}
