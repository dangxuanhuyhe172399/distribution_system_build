package com.sep490.bads.distributionsystem.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class LoginDto {
    private String username;
    private String password;
}
