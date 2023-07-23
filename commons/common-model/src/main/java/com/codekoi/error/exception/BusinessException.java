package com.codekoi.error.exception;


import com.codekoi.error.ErrorInfo;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorInfo errorInfo;

    public BusinessException(ErrorInfo errorInfo) {
        super(errorInfo.getMessage());
        this.errorInfo = errorInfo;
    }
}
