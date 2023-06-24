package codekoi.apiserver.global.token.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class RefreshTokenExpiredException extends InvalidValueException {
    public RefreshTokenExpiredException() {
        super(ErrorInfo.TOKEN_EXPIRED_REFRESH_TOKEN);

    }
}
