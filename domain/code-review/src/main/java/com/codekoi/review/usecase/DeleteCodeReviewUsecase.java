package com.codekoi.review.usecase;

public interface DeleteCodeReviewUsecase {

    void command(Command command);

    record Command(
            Long codeReviewId
    ) {
    }
}
