package codekoi.apiserver.domain.auth.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class AuthorizationException extends InvalidValueException {
    public AuthorizationException(ErrorInfo errorInfo) {
        super(errorInfo);
    }
}
