package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserFilterDto {
    private String fullName;
    private UserStatus status;
    private LocalDate from;        // filter createdAt >= from
    private LocalDate to;          // filter createdAt <  to+1
}