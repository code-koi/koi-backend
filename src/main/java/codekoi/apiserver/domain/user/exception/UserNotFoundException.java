package codekoi.apiserver.domain.user.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class UserNotFoundException extends InvalidValueException {
    public UserNotFoundException() {
        super(ErrorInfo.USER_NOT_FOUND);
    }
}
