package com.sep490.bads.distributionsystem.dto.customerDto;

import com.sep490.bads.distributionsystem.entity.type.CustomerStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerFilterDto {
    private String q;                       // tìm theo code/name/phone/email/tax/address/…
    private CustomerStatus status;          // ACTIVE / INACTIVE / BLOCKED (ẩn DELETED khỏi list)
    private Long customerTypeId;            // lọc theo loại

    // lọc theo số dư hiện tại
    @PositiveOrZero private Long minBalance;   // tính theo VND -> quy sang BigDecimal
    @PositiveOrZero private Long maxBalance;

    // sort + paging
    private String sortBy = "createdAt";
    private String direction = "DESC";
    @Min(0) private int page = 0;
    @Min(1) private int size = 10;
}
