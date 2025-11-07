package com.sep490.bads.distributionsystem.dto.CustomerDto;

import com.sep490.bads.distributionsystem.entity.type.CustomerStatus;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
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