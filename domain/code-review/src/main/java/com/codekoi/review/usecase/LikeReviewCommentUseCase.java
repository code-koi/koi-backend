package com.codekoi.review.usecase;

public interface LikeReviewCommentUseCase {

    void command(Command command);

    record Command(
            Long userId,
            Long commentId
    ) {
    }
}
