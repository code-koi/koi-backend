package com.codekoi.domain.like.service;

import com.codekoi.domain.comment.entity.ReviewComment;
import com.codekoi.domain.comment.repository.ReviewCommentCoreRepository;
import com.codekoi.domain.like.entity.Like;
import com.codekoi.domain.like.exception.NotLikedCommentException;
import com.codekoi.domain.like.repository.LikeCoreRepository;
import com.codekoi.domain.like.usecase.UnlikeReviewCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UnlikeReviewComment implements UnlikeReviewCommentUseCase {

    private final ReviewCommentCoreRepository reviewCommentCoreRepository;
    private final LikeCoreRepository likeCoreRepository;

    @Override
    public void command(Command command) {
        final Long commentId = command.commentId();
        final ReviewComment reviewComment = reviewCommentCoreRepository.getOneById(commentId);

        final Long userId = command.userId();
        final Like like = likeCoreRepository.findByUserIdAndCommentId(userId, commentId)
                .orElseThrow(NotLikedCommentException::new);

        reviewComment.minusLikeOne();
        likeCoreRepository.delete(like);
    }
}
