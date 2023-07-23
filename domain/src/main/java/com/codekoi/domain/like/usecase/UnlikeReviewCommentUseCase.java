package com.codekoi.domain.like.usecase;

public interface UnlikeReviewCommentUseCase {

    void command(Command command);

    record Command(
            Long userId,
            Long commentId
    ) {
    }

}
