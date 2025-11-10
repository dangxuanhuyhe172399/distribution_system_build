package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.SupplierCategory;
import com.sep490.bads.distributionsystem.entity.type.SupplierStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierDto {
    private Long id;
    private String name;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private String taxCode;
    private SupplierCategory category;
    private SupplierStatus status;
}
