package com.sep490.bads.distributionsystem.dto;

import lombok.Getter;
import lombok.Setter;

import com.sep490.bads.distributionsystem.entity.type.RoleStatus;

@Getter
@Setter
public class RoleUpdateDto {
    private String code;
    private String description;
    private RoleStatus status;
}
