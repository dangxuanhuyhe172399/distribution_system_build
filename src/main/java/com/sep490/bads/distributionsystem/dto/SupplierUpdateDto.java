package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierUpdateDto {
    private String name;
    private String contactName;
    private String phone;
    @Email
    private String email;
    private String address;
    private String taxCode;
    private Long categoryId;
    private CommonStatus status;
}
