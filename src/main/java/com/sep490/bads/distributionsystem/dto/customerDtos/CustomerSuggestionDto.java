package com.sep490.bads.distributionsystem.dto.customerDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSuggestionDto {
    private Long id;
    private String code;
    private String name;
    private String phone;
    private String address;
    private String district;
    private String province;
    private String customerTypeName;
    private BigDecimal currentBalance;

    // thời điểm đơn hàng gần nhất (FE format ra "2 phút trước")
    private String lastOrderAt;
}
