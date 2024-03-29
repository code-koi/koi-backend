package com.codekoi.review.usecase;

public interface CreateReviewCommentUseCase {

    Long command(Command command);

    record Command(
            Long reviewId,
            Long userId,
            String content
    ) {

    }
}
