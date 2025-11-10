package com.sep490.bads.distributionsystem.dto.customerDtos;

import com.sep490.bads.distributionsystem.entity.type.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomersDto {
    private Long id;
    private String code;

    private String name;
    private String phone;
    private String email;
    private String taxCode;

    private String address;
    private String district;
    private String province;

    private Long   customerTypeId;
    private String customerTypeName;

    private BigDecimal balanceLimit;   // hạn mức công nợ
    private BigDecimal currentBalance; // số dư hiện tại (tiền KH đang có)

    private CustomerStatus status;
    private String note;
}