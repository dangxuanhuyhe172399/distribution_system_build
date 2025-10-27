package com.sep490.bads.distributionsystem.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerDto {
    private Integer customerId;
    private String name;
    private String address;
    private Integer typeId;
    private String email;
    private String phone;
    private String taxCode;
    private Boolean status;
    private LocalDateTime createdAt;
    private Integer createdBy;
    private Integer updatedBy;
    private LocalDateTime updatedAt;
}