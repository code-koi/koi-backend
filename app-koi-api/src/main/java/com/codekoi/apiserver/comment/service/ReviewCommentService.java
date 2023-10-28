package com.codekoi.apiserver.comment.service;

import com.codekoi.domain.comment.usecase.CreateReviewCommentUseCase;
import com.codekoi.domain.comment.usecase.DeleteReviewCommentUseCase;
import com.codekoi.domain.comment.usecase.UpdateReviewCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewCommentService {

    private final CreateReviewCommentUseCase createReviewCommentUseCase;
    private final DeleteReviewCommentUseCase deleteReviewCommentUseCase;
    private final UpdateReviewCommentUseCase updateReviewCommentUseCase;

    public Long create(Long reviewId, Long userId, String content) {
        return createReviewCommentUseCase.command(new CreateReviewCommentUseCase.Command(reviewId, userId, content));
    }

    //todo: 해당 유저만 삭제하도록 변경
    public void delete(Long commentId, Long userId) {
        deleteReviewCommentUseCase.command(new DeleteReviewCommentUseCase.Command(commentId));
    }

    public void update(Long commentId, Long userId, String content) {
        updateReviewCommentUseCase.command(new UpdateReviewCommentUseCase.Command(userId, commentId, content));
    }
}
