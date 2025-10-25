package com.sep490.bads.distributionsystem.exception;

import com.sep490.bads.distributionsystem.exception.base.BaseException;
import com.sep490.bads.distributionsystem.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthorizedException extends BaseException {
    private static final long serialVersionUID = 1L;

    public NotAuthorizedException() {
        super("not authorized", ErrorCode.UNAUTHORIZED);
    }

    public NotAuthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }
}
