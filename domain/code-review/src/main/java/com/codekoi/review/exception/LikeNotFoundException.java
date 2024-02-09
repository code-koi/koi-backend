package com.codekoi.review.exception;

import com.codekoi.error.ErrorInfo;
import com.codekoi.error.exception.BusinessException;

public class LikeNotFoundException extends BusinessException {
    public LikeNotFoundException() {
        super(ErrorInfo.LIKE_NOT_FOUND);
    }
}
