package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private Long createdAt;
    private Long updatedAt;
}