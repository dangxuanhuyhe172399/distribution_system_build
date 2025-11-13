package com.sep490.bads.distributionsystem.dto;


import com.sep490.bads.distributionsystem.entity.type.SupplierStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierFilterDto {
    private String keyword;        // tìm kiếm theo tên, email, phone, taxCode
    private Long categoryId;       // lọc theo danh mục
    private SupplierStatus status;   // lọc theo trạng thái
    private int page = 0;
    private int size = 10;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";
}
