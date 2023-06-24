package codekoi.apiserver.global.token.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class AuthorizationNotExistException extends InvalidValueException {
    public AuthorizationNotExistException() {
        super(ErrorInfo.TOKEN_NOT_EXIST);
    }
}
