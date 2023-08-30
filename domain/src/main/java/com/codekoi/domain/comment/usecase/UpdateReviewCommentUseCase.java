package com.codekoi.domain.comment.usecase;

public interface UpdateReviewCommentUseCase {

    void command(Command command);

    record Command(
            Long userId,
            Long commentId,
            String content
    ) {
    }
}
