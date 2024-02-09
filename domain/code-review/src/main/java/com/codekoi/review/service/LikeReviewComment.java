package com.codekoi.review.service;

import com.codekoi.review.CommentLike;
import com.codekoi.review.CommentLikeRepository;
import com.codekoi.review.ReviewComment;
import com.codekoi.review.ReviewCommentRepository;
import com.codekoi.review.exception.AlreadyLikedCommentException;
import com.codekoi.review.usecase.LikeReviewCommentUseCase;
import com.codekoi.user.User;
import com.codekoi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
class LikeReviewComment implements LikeReviewCommentUseCase {

    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    @Override
    public void command(Command command) {
        final Long userId = command.userId();
        final User user = userRepository.getOneById(userId);

        final Long commentId = command.commentId();
        checkAlreadyLikedComment(userId, commentId);

        final ReviewComment comment = reviewCommentRepository.getOneById(commentId);

        comment.addLikeOne();

        final CommentLike commentLike = CommentLike.of(user, comment);
        commentLikeRepository.save(commentLike);
    }

    private void checkAlreadyLikedComment(Long userId, Long commentId) {
        final Optional<CommentLike> likeOptional = commentLikeRepository.findByUserIdAndCommentId(userId, commentId);
        if (likeOptional.isPresent()) {
            throw new AlreadyLikedCommentException();
        }
    }
}
