package com.sep490.bads.distributionsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthRequest {
    private String userEmail;
    private String password;
}
