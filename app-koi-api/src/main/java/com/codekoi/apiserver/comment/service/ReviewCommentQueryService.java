package com.codekoi.apiserver.comment.service;

import com.codekoi.apiserver.comment.dto.CommentReviewDetailDto;
import com.codekoi.apiserver.comment.dto.UserCodeCommentDto;
import com.codekoi.apiserver.comment.repository.ReviewCommentRepository;
import com.codekoi.apiserver.koi.repository.KoiHistoryRepository;
import com.codekoi.apiserver.like.repository.LikeRepository;
import com.codekoi.domain.comment.entity.ReviewComment;
import com.codekoi.domain.koi.entity.KoiHistory;
import com.codekoi.domain.like.entity.Like;
import com.codekoi.domain.review.entity.CodeReview;
import com.codekoi.domain.review.repository.CodeReviewCoreRepository;
import com.codekoi.domain.user.entity.User;
import com.codekoi.domain.user.repository.UserCoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewCommentQueryService {

    private final ReviewCommentRepository reviewCommentRepository;
    private final LikeRepository likeRepository;
    private final KoiHistoryRepository koiHistoryRepository;

    private final CodeReviewCoreRepository codeReviewCoreRepository;
    private final UserCoreRepository userCoreRepository;

    public List<UserCodeCommentDto> getUserComments(Long userId) {
        final User user = userCoreRepository.getOneById(userId);
        final List<ReviewComment> comments = reviewCommentRepository.findByUserId(user.getId());

        final List<KoiHistory> koiHistories = koiHistoryRepository.findUserCommentKoiHistory(
                comments.stream()
                        .map(ReviewComment::getId)
                        .collect(Collectors.toList())
        );

        final List<Long> commentIds = extractCommentIds(comments);
        final List<Like> likes = likeRepository.findByCommentIdIn(commentIds);

        return UserCodeCommentDto.listOf(user, comments, koiHistories, likes);
    }

    public List<CommentReviewDetailDto> getCommentsOnReview(Long sessionUserId, Long reviewId) {
        final CodeReview codeReview = codeReviewCoreRepository.getOneById(reviewId);
        final List<ReviewComment> comments = reviewCommentRepository.findByCodeReviewId(codeReview.getId());

        final List<Long> commentIds = extractCommentIds(comments);
        final List<Like> likes = likeRepository.findByCommentIdIn(commentIds);

        final List<KoiHistory> koiHistories = koiHistoryRepository.findUserCommentKoiHistory(commentIds);

        return CommentReviewDetailDto.listOf(comments, koiHistories, sessionUserId, likes);
    }

    private static List<Long> extractCommentIds(List<ReviewComment> comments) {
        return comments
                .stream()
                .map(ReviewComment::getId)
                .collect(Collectors.toList());
    }
}
