package com.sep490.bads.distributionsystem.exception.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {
  protected int code;

  public BaseException(String message, int code) {
        super(message);
        setCode(code);
    }
}
