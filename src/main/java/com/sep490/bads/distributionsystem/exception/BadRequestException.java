package com.sep490.bads.distributionsystem.exception;

import com.sep490.bads.distributionsystem.exception.base.BaseException;
import com.sep490.bads.distributionsystem.exception.base.ErrorCode;
import com.sep490.bads.distributionsystem.exception.base.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends BaseException {
    private static final long serialVersionUID = 1L;

    public BadRequestException(String exception) {
        super(exception, ErrorCode.BAD_REQUEST);
    }
    public BadRequestException(Integer code) {
        super("Error", code);
    }
    public BadRequestException(String exception, Integer code) {
        super(exception, code);
    }

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        return ResponseEntity
                .status(ex.getCode() == ErrorCode.NOT_FOUND ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getCode(), ex.getMessage()));
    }

}
