package com.sep490.bads.distributionsystem.exception;

import com.sep490.bads.distributionsystem.exception.base.BaseException;
import com.sep490.bads.distributionsystem.exception.base.ErrorCode;

public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }
}
