package codekoi.apiserver.domain.code.like.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class LikeUserNotMatchedException extends InvalidValueException {
    public LikeUserNotMatchedException() {
        super(ErrorInfo.LIKE_USER_NOT_MATCHED);

    }
}
