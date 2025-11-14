package com.sep490.bads.distributionsystem.dto.supplierDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSupplierRequestDto {
    private String name;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private String taxCode;
    private Long categoryId;
    private Integer paymentTermDays; // Hạn thanh toán (ngày).
    private String note;             // optional
}
