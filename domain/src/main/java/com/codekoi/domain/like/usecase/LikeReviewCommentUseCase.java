package com.codekoi.domain.like.usecase;

public interface LikeReviewCommentUseCase {

    void command(Command command);

    record Command(
            Long userId,
            Long commentId
    ) {
    }
}
