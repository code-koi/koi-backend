package com.codekoi.review.usecase;

public interface UpdateReviewCommentUseCase {

    void command(Command command);

    record Command(
            Long userId,
            Long commentId,
            String content
    ) {
    }
}
