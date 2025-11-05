package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDto {
    private Long id;
    private String name;
    private String address;

    private Long categoryId;
    private String categoryName;

    private String email;
    private String phone;
    private String taxCode;
    private CommonStatus status;
}
