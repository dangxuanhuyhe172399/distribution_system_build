package com.sep490.bads.distributionsystem.dto;

import com.sep490.bads.distributionsystem.entity.type.UserStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserFilterDto {
    private String q;
    private UserStatus status;
    private Long roleId;                            // lọc role
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate from; // >= from 00:00
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate to;   // <  to+1 00:00
}