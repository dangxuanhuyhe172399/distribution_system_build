package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.security.service.UserDetailsImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;

@Log4j2
public abstract class BaseController {
    protected UserDetailsImpl getUserDetails(Authentication authentication) {
        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
