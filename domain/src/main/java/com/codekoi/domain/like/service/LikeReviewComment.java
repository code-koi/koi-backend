package com.codekoi.domain.like.service;

import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.comment.ReviewCommentRepository;
import com.codekoi.domain.like.Like;
import com.codekoi.domain.like.exception.AlreadyLikedCommentException;
import com.codekoi.domain.like.LikeRepository;
import com.codekoi.domain.like.usecase.LikeReviewCommentUseCase;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeReviewComment implements LikeReviewCommentUseCase {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    @Override
    public void command(Command command) {
        final Long userId = command.userId();
        final User user = userRepository.getOneById(userId);

        final Long commentId = command.commentId();
        checkAlreadyLikedComment(userId, commentId);

        final ReviewComment comment = reviewCommentRepository.getOneById(commentId);

        comment.addLikeOne();

        final Like like = Like.of(user, comment);
        likeRepository.save(like);
    }

    private void checkAlreadyLikedComment(Long userId, Long commentId) {
        final Optional<Like> likeOptional = likeRepository.findByUserIdAndCommentId(userId, commentId);
        if (likeOptional.isPresent()) {
            throw new AlreadyLikedCommentException();
        }
    }
}
