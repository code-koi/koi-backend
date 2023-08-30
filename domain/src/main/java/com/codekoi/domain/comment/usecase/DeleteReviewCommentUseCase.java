package com.codekoi.domain.comment.usecase;

public interface DeleteReviewCommentUseCase {

    void command(Command command);

    record Command(
            Long commentId
    ) {
    }

}
