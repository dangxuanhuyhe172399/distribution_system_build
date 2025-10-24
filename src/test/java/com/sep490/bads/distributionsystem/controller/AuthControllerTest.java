package com.sep490.bads.distributionsystem.controller;

import com.sep490.bads.distributionsystem.controller.common.AuthController;
import com.sep490.bads.distributionsystem.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AuthControllerTest {
    @Mock
    AuthService authService;
    @InjectMocks
    AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testLogin() {

    }

    @Test
    void testForgotPassword() {

    }
}

