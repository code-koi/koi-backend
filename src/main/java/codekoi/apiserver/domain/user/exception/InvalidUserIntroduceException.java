package codekoi.apiserver.domain.user.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class InvalidUserIntroduceException extends InvalidValueException {
    public InvalidUserIntroduceException() {
        super(ErrorInfo.USER_INTRODUCE_OVER_ERROR);
    }
}
