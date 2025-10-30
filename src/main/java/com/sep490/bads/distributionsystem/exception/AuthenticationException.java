package com.sep490.bads.distributionsystem.exception;

import com.sep490.bads.distributionsystem.exception.base.BaseException;
import com.sep490.bads.distributionsystem.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }
    public AuthenticationException(Integer code) {
        super("Error", code);
    }
    public AuthenticationException(String exception, Integer code) {
        super(exception, code);
    }

}
