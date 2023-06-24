package codekoi.apiserver.global.token.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class AccessTokenExpiredException extends InvalidValueException {
    public AccessTokenExpiredException() {
        super(ErrorInfo.TOKEN_EXPIRED_ACCESS_TOKEN);
    }
}
