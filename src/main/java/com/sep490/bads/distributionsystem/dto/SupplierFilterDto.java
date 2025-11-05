package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierFilterDto {
    private String keyword;
    private Long categoryId;
    private CommonStatus status;

    private String sortBy = "createdAt";
    private String direction = "DESC";

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;
}
