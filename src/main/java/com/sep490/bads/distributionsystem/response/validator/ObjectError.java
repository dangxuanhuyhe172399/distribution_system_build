package com.sep490.bads.distributionsystem.response.validator;

import lombok.Data;

@Data
public class ObjectError {
    private String code;
    private String msgError;

    public ObjectError(String code, String msgError) {
        this.code = code;
        this.msgError = msgError;
    }
}
