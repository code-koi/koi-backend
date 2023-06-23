package codekoi.apiserver.domain.user.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class InvalidUserYearsException extends InvalidValueException {
    public InvalidUserYearsException() {
        super(ErrorInfo.USER_YEARS_ERROR);
    }
}
