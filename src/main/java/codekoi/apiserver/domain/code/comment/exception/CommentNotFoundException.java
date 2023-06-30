package codekoi.apiserver.domain.code.comment.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class CommentNotFoundException extends InvalidValueException {
    public CommentNotFoundException() {
        super(ErrorInfo.COMMENT_NOT_FOUND);
    }
}
