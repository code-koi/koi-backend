package codekoi.apiserver.domain.user.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class InvalidUserNicknameException extends InvalidValueException {
    public InvalidUserNicknameException() {
        super(ErrorInfo.USER_NICKNAME_OVER);
    }
}
