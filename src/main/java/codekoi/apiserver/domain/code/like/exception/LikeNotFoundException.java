package codekoi.apiserver.domain.code.like.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class LikeNotFoundException extends InvalidValueException {
    public LikeNotFoundException() {
        super(ErrorInfo.LIKE_NOT_FOUND);
    }
}
