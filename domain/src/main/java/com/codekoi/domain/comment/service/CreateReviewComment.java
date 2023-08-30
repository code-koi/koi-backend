package com.codekoi.domain.comment.service;

import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.comment.ReviewCommentRepository;
import com.codekoi.domain.comment.usecase.CreateReviewCommentUseCase;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
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

        final ReviewComment reviewComment = ReviewComment.of(user, codeReview, command.content());
        reviewCommentRepository.save(reviewComment);

        return reviewComment.getId();
    }
}
