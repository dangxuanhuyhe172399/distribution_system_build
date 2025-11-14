package com.sep490.bads.distributionsystem.dto.supplierDtos;

import com.sep490.bads.distributionsystem.entity.type.SupplierStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDetailInfoDto {
    private Long id;
    private String code;          // NCC001
    private String name;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private String taxCode;

    private String categoryName;
    private SupplierStatus status;

    private Integer paymentTermDays;     // Hạn thanh toán (ngày).
    private String note;

    private String createdByName;
    private String createdAt;            // format dd/MM/yyyy
    private String lastPurchaseDate;     // mua hàng gần nhất (dd/MM/yyyy) - có thể null
}
