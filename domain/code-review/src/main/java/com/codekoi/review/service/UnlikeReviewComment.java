package com.codekoi.review.service;

import com.codekoi.review.CommentLike;
import com.codekoi.review.CommentLikeRepository;
import com.codekoi.review.ReviewComment;
import com.codekoi.review.ReviewCommentRepository;
import com.codekoi.review.exception.NotLikedCommentException;
import com.codekoi.review.usecase.UnlikeReviewCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class UnlikeReviewComment implements UnlikeReviewCommentUseCase {

    private final ReviewCommentRepository reviewCommentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public void command(Command command) {
        final Long commentId = command.commentId();
        final ReviewComment reviewComment = reviewCommentRepository.getOneById(commentId);

        final Long userId = command.userId();
        final CommentLike commentLike = commentLikeRepository.findByUserIdAndCommentId(userId, commentId)
                .orElseThrow(NotLikedCommentException::new);

        reviewComment.minusLikeOne();
        commentLikeRepository.delete(commentLike);
    }
}
