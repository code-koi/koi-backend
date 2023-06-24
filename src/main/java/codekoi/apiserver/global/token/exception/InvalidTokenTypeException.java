package codekoi.apiserver.global.token.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class InvalidTokenTypeException extends InvalidValueException {
    public InvalidTokenTypeException() {
        super(ErrorInfo.TOKEN_INVALID_TYPE);
    }
}
