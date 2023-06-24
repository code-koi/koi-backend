package codekoi.apiserver.domain.user.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class InvalidUserEmailException extends InvalidValueException {
    public InvalidUserEmailException() {
        super(ErrorInfo.USER_EMAIL_INVALID);
    }
}
