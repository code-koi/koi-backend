package codekoi.apiserver.domain.code.like.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class AlreadyLikedCommentException extends InvalidValueException {
    public AlreadyLikedCommentException() {
        super(ErrorInfo.ALREADY_LIKED_COMMENT);
    }
}
