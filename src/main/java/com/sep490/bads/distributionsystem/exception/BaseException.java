package com.sep490.bads.distributionsystem.exception;

public class BaseException extends RuntimeException {
    private final String message;
    private final int statusCode;

    public BaseException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
