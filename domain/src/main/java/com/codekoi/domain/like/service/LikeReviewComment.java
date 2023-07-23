package com.codekoi.domain.like.service;

import com.codekoi.domain.comment.entity.ReviewComment;
import com.codekoi.domain.comment.repository.ReviewCommentCoreRepository;
import com.codekoi.domain.like.entity.Like;
import com.codekoi.domain.like.exception.AlreadyLikedCommentException;
import com.codekoi.domain.like.repository.LikeCoreRepository;
import com.codekoi.domain.like.usecase.LikeReviewCommentUseCase;
import com.codekoi.domain.user.entity.User;
import com.codekoi.domain.user.repository.UserCoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeReviewComment implements LikeReviewCommentUseCase {

    private final UserCoreRepository userCoreRepository;
    private final LikeCoreRepository likeCoreRepository;
    private final ReviewCommentCoreRepository reviewCommentCoreRepository;

    @Override
    public void command(Command command) {
        final Long userId = command.userId();
        final User user = userCoreRepository.getOneById(userId);

        final Long commentId = command.commentId();
        checkAlreadyLikedComment(userId, commentId);

        final ReviewComment comment = reviewCommentCoreRepository.getOneById(commentId);

        comment.addLikeOne();

        final Like like = Like.of(user, comment);
        likeCoreRepository.save(like);
    }

    private void checkAlreadyLikedComment(Long userId, Long commentId) {
        final Optional<Like> likeOptional = likeCoreRepository.findByUserIdAndCommentId(userId, commentId);
        if (likeOptional.isPresent()) {
            throw new AlreadyLikedCommentException();
        }
    }
}
