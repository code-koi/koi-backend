package com.codekoi.domain.comment.usecase;

public interface CreateReviewCommentUseCase {

    Long command(Command command);

    record Command(
            Long reviewId,
            Long userId,
            String content
    ) {

    }
}
