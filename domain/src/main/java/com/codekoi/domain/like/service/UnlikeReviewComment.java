package com.codekoi.domain.like.service;

import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.comment.ReviewCommentRepository;
import com.codekoi.domain.like.Like;
import com.codekoi.domain.like.LikeRepository;
import com.codekoi.domain.like.exception.NotLikedCommentException;
import com.codekoi.domain.like.usecase.UnlikeReviewCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class UnlikeReviewComment implements UnlikeReviewCommentUseCase {

    private final ReviewCommentRepository reviewCommentRepository;
    private final LikeRepository likeRepository;

    @Override
    public void command(Command command) {
        final Long commentId = command.commentId();
        final ReviewComment reviewComment = reviewCommentRepository.getOneById(commentId);

        final Long userId = command.userId();
        final Like like = likeRepository.findByUserIdAndCommentId(userId, commentId)
                .orElseThrow(NotLikedCommentException::new);

        reviewComment.minusLikeOne();
        likeRepository.delete(like);
    }
}
