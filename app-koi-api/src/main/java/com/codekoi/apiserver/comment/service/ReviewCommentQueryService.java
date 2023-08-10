package com.codekoi.apiserver.comment.service;

import com.codekoi.apiserver.comment.dto.CommentReviewDetailDto;
import com.codekoi.apiserver.comment.dto.UserCodeCommentDto;
import com.codekoi.apiserver.comment.repository.ReviewCommentQueryRepository;
import com.codekoi.apiserver.koi.repository.KoiHistoryQueryRepository;
import com.codekoi.apiserver.like.repository.LikeQueryRepository;
import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.koi.KoiHistory;
import com.codekoi.domain.like.Like;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewCommentQueryService {

    private final ReviewCommentQueryRepository reviewCommentQueryRepository;
    private final LikeQueryRepository likeQueryRepository;
    private final KoiHistoryQueryRepository koiHistoryQueryRepository;

    private final CodeReviewRepository codeReviewRepository;
    private final UserRepository userRepository;

    public List<UserCodeCommentDto> getUserComments(Long userId) {
        final User user = userRepository.getOneById(userId);
        final List<ReviewComment> comments = reviewCommentQueryRepository.findByUserId(user.getId());

        final List<KoiHistory> koiHistories = koiHistoryQueryRepository.findUserCommentKoiHistory(
                comments.stream()
                        .map(ReviewComment::getId)
                        .collect(Collectors.toList())
        );

        final List<Long> commentIds = extractCommentIds(comments);
        final List<Like> likes = likeQueryRepository.findByCommentIdIn(commentIds);

        return UserCodeCommentDto.listOf(user, comments, koiHistories, likes);
    }

    public List<CommentReviewDetailDto> getCommentsOnReview(Long sessionUserId, Long reviewId) {
        final CodeReview codeReview = codeReviewRepository.getOneById(reviewId);
        final List<ReviewComment> comments = reviewCommentQueryRepository.findByCodeReviewId(codeReview.getId());

        final List<Long> commentIds = extractCommentIds(comments);
        final List<Like> likes = likeQueryRepository.findByCommentIdIn(commentIds);

        final List<KoiHistory> koiHistories = koiHistoryQueryRepository.findUserCommentKoiHistory(commentIds);

        return CommentReviewDetailDto.listOf(comments, koiHistories, sessionUserId, likes);
    }

    private static List<Long> extractCommentIds(List<ReviewComment> comments) {
        return comments
                .stream()
                .map(ReviewComment::getId)
                .collect(Collectors.toList());
    }
}
