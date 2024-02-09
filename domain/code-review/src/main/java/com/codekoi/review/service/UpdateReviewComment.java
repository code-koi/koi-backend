package com.codekoi.review.service;

import com.codekoi.review.ReviewComment;
import com.codekoi.review.ReviewCommentRepository;
import com.codekoi.review.usecase.UpdateReviewCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class UpdateReviewComment implements UpdateReviewCommentUseCase {

    private final ReviewCommentRepository reviewCommentRepository;

    @Override
    public void command(Command command) {
        final ReviewComment reviewComment = reviewCommentRepository.getOneById(command.commentId());

        if (!reviewComment.isMyComment(command.userId())) {
            return;
        }

        reviewComment.update(command.content());
    }
}
