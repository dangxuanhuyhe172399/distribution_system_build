package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import lombok.*;

@Data
@Builder
public class CustomerFilterDto {

    private String keyword; // tìm theo tên hoặc email
    private Long typeId;    // loại khách hàng
    private CommonStatus status; // ACTIVE / INACTIVE

    private String sortBy = "createdAt";
    private String direction = "DESC";

    private int page = 0;
    private int size = 10;
}
