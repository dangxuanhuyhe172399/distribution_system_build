package com.tal.recruitment.system.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthRequest {
    private String userEmail;
    private String password;
}
