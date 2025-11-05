package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerDto {
    private Long id;
    private String name;
    private String address;

    private Long typeId;
    private String typeName;

    private String email;
    private String phone;
    private String taxCode;

    private CommonStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}