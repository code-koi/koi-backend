package com.codekoi.review.exception;

import com.codekoi.error.ErrorInfo;
import com.codekoi.error.exception.BusinessException;

public class NotLikedCommentException extends BusinessException {
    public NotLikedCommentException() {
        super(ErrorInfo.NOT_LIKED_COMMENT);
    }
}
