package com.codekoi.domain.comment.service;

import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.comment.ReviewCommentRepository;
import com.codekoi.domain.comment.usecase.UpdateReviewCommentUseCase;
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
