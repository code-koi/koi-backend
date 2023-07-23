package com.codekoi.coreweb.jwt.exception;


import com.codekoi.error.ErrorInfo;
import com.codekoi.error.exception.BusinessException;

public class AuthorizationNotExistException extends BusinessException {
    public AuthorizationNotExistException() {
        super(ErrorInfo.TOKEN_NOT_EXIST);
    }
}
