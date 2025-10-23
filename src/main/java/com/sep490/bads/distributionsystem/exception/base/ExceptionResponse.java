package com.sep490.bads.distributionsystem.exception.base;

import com.sep490.bads.distributionsystem.exception.BaseException;

public class ExceptionResponse {
    private String message;
    private int code;

    // Constructor
    public ExceptionResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public static Object createFrom(BaseException e) {

        return null;
    }

    // Factory method để tạo nhanh
//    public static ExceptionResponse createFrom(BaseException ex) {
//        return new ExceptionResponse(ex.getMessage(), ex.getStatusCode());
//    }

    // Getters
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
