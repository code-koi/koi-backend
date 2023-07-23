package com.codekoi.domain.authtoken.exception;

import com.codekoi.error.ErrorInfo;
import com.codekoi.error.exception.BusinessException;

public class RefreshTokenNotMatchedException extends BusinessException {
    public RefreshTokenNotMatchedException() {
        super(ErrorInfo.TOKEN_NOT_MATCHED);
    }
}
