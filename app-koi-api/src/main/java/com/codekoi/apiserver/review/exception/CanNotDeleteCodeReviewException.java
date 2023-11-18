package com.codekoi.apiserver.review.exception;

import com.codekoi.error.ErrorInfo;
import com.codekoi.error.exception.BusinessException;

public class CanNotDeleteCodeReviewException extends BusinessException {
    public CanNotDeleteCodeReviewException() {
        super(ErrorInfo.CAN_NOT_DELETE_CODE_REVIEW);
    }
}
