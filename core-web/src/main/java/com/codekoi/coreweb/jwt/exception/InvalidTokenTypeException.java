package com.codekoi.coreweb.jwt.exception;

import com.codekoi.error.ErrorInfo;
import com.codekoi.error.exception.BusinessException;

public class InvalidTokenTypeException extends BusinessException {
    public InvalidTokenTypeException() {
        super(ErrorInfo.TOKEN_INVALID_TYPE);
    }
}
