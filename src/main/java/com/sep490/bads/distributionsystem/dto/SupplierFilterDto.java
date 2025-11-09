package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierFilterDto {
    private String keyword;
    private Long categoryId;
    private CommonStatus status;
    private int page = 0;
    private int size = 10;
}
