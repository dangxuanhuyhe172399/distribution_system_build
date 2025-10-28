package com.sep490.bads.distributionsystem.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sep490.bads.distributionsystem.response.validator.ObjectError;
import org.springframework.http.*;

public class ResultResponse<T> extends ResponseEntity<ResultResponse.BodyData<T>> {

    private ResultResponse(HttpStatus status, BodyData<T> body) {
        super(body, status);
    }

    // 200
    public static <T> ResultResponse<T> success(T data) {
        return new ResultResponse<>(HttpStatus.OK, new BodyData<>("OK", "OK", data));
    }

    // 201 (có body)
    public static <T> ResultResponse<T> created(T data) {
        return new ResultResponse<>(HttpStatus.CREATED, new BodyData<>("CREATED", "Created", data));
    }

    // 400
    public static ResultResponse<Object> badRequest(ObjectError err) {
        String code = err.getCode() != null ? err.getCode() : "BAD_REQUEST";
        String msg  = err.getMsgError() != null ? err.getMsgError() : "Bad Request";
        return new ResultResponse<>(HttpStatus.BAD_REQUEST, new BodyData<>(code, msg, null));
    }

    // 500
    public static ResultResponse<Object> serverError(String msg) {
        return new ResultResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, new BodyData<>("INTERNAL_ERROR", msg, null));
    }

    public static class BodyData<T> {
        @JsonProperty("code")    private final String code;
        @JsonProperty("message") private final String message;
        @JsonProperty("data")    private final T data;
        public BodyData(String code, String message, T data) {
            this.code = code; this.message = message; this.data = data;
        }
        public String getCode(){return code;}
        public String getMessage(){return message;}
        public T getData(){return data;}
    }
}
