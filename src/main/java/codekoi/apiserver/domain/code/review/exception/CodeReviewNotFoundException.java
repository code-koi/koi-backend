package codekoi.apiserver.domain.code.review.exception;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;

public class CodeReviewNotFoundException extends InvalidValueException {
    public CodeReviewNotFoundException() {
        super(ErrorInfo.CODE_REVIEW_NOT_FOUND);

    }
}
