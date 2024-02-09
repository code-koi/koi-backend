package com.codekoi.review.exception;

import com.codekoi.error.ErrorInfo;
import com.codekoi.error.exception.BusinessException;

public class AlreadyLikedCommentException extends BusinessException {
    public AlreadyLikedCommentException() {
        super(ErrorInfo.ALREADY_LIKED_COMMENT);
    }
}
