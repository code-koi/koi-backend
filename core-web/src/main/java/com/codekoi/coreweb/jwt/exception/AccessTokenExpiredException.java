package com.codekoi.coreweb.jwt.exception;

import com.codekoi.error.ErrorInfo;
import com.codekoi.error.exception.BusinessException;

public class AccessTokenExpiredException extends BusinessException {
    public AccessTokenExpiredException() {
        super(ErrorInfo.TOKEN_EXPIRED_ACCESS_TOKEN);
    }
}
