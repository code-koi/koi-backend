package com.codekoi.review.service;

import com.codekoi.review.CodeReview;
import com.codekoi.review.CodeReviewRepository;
import com.codekoi.review.ReviewComment;
import com.codekoi.review.ReviewCommentRepository;
import com.codekoi.review.usecase.CreateReviewCommentUseCase;
import com.codekoi.user.User;
import com.codekoi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class CreateReviewComment implements CreateReviewCommentUseCase {

    private final UserRepository userRepository;
    private final CodeReviewRepository codeReviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    @Override
    public Long command(Command command) {
        final User user = userRepository.getOneById(command.userId());
        final CodeReview codeReview = codeReviewRepository.getOneById(command.reviewId());
        final ReviewComment parentComment = reviewCommentRepository.getOneById((command.parentId()));

        final ReviewComment reviewComment = ReviewComment.of(user, codeReview, parentComment, command.content());
        reviewCommentRepository.save(reviewComment);

        return reviewComment.getId();
    }
}
