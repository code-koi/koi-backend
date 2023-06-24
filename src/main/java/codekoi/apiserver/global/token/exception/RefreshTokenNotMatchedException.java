package codekoi.apiserver.global.token.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class RefreshTokenNotMatchedException extends InvalidValueException {
    public RefreshTokenNotMatchedException() {
        super(ErrorInfo.TOKEN_NOT_MATCHED);
    }
}
