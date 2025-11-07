package com.sep490.bads.distributionsystem.dto.CustomerDto;

import com.sep490.bads.distributionsystem.entity.type.CustomerStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerUpdateDto {
    @Size(max = 100)
    private String name;
    @Size(max = 20)
    private String phone;
    @Email @Size(max = 100)
    private String email;
    @Size(max = 50)
    private String taxCode;

    @Size(max = 255)
    private String address;
    @Size(max = 100)
    private String district;
    @Size(max = 100)
    private String province;

    private Long customerTypeId;

    @PositiveOrZero
    private BigDecimal balanceLimit;

    @PositiveOrZero
    private BigDecimal currentBalance;

    private CustomerStatus status;
    @Size(max = 255)
    private String note;
}
