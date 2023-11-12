package com.codekoi.domain.review.usecase;

public interface DeleteCodeReviewUsecase {

    void command(Command command);

    record Command(
            Long codeReviewId
    ) {
    }
}
