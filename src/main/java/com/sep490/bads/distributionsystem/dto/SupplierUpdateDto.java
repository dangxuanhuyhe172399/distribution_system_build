package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierUpdateDto {
    private String name;
    @Email
    private String email;
    private String phone;
    private String address;
    private String taxCode;
    private Long categoryId;
    private CommonStatus status;
}
