package com.codekoi.domain.comment.service;

import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.comment.ReviewCommentRepository;
import com.codekoi.domain.comment.usecase.DeleteReviewCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class DeleteReviewComment implements DeleteReviewCommentUseCase {

    private final ReviewCommentRepository reviewCommentRepository;

    @Override
    public void command(Command command) {
        final ReviewComment reviewComment = reviewCommentRepository.getOneById(command.commentId());

        reviewCommentRepository.delete(reviewComment);
    }
}
