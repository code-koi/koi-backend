package com.codekoi.auth.exception;

import com.codekoi.error.ErrorInfo;
import com.codekoi.error.exception.BusinessException;

public class RefreshTokenNotAppliedException extends BusinessException {
    public RefreshTokenNotAppliedException() {
        super(ErrorInfo.TOKEN_REFRESH_TOKEN_NOT_APPLIED);
    }
}
