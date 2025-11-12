package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDto {
    private Long id;
    private String name;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private String taxCode;
    private CommonStatus status;

    private Long categoryId;
    private String categoryName;

    private Long createdById;
    private String createdByName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
