package com.codekoi.coreweb.jwt.exception;

import com.codekoi.error.ErrorInfo;
import com.codekoi.error.exception.BusinessException;

public class RefreshTokenExpiredException extends BusinessException {
    public RefreshTokenExpiredException() {
        super(ErrorInfo.TOKEN_EXPIRED_REFRESH_TOKEN);
    }
}
