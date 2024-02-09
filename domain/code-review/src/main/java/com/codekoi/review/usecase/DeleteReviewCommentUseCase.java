package com.codekoi.review.usecase;

public interface DeleteReviewCommentUseCase {

    void command(Command command);

    record Command(
            Long commentId
    ) {
    }

}
