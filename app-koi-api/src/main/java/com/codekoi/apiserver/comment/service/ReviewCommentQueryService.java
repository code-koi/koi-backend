package com.codekoi.apiserver.comment.service;

import com.codekoi.apiserver.comment.dto.CommentReviewDetailDto;
import com.codekoi.apiserver.comment.dto.HotReviewComment;
import com.codekoi.apiserver.comment.dto.UserCodeCommentDto;
import com.codekoi.koi.KoiHistory;
import com.codekoi.koi.KoiHistoryRepository;
import com.codekoi.review.*;
import com.codekoi.user.User;
import com.codekoi.user.UserRepository;
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
    private final CommentLikeRepository commentLikeRepository;
    private final KoiHistoryRepository koiHistoryRepository;

    private final CodeReviewRepository codeReviewRepository;
    private final UserRepository userRepository;

    public List<UserCodeCommentDto> getUserComments(Long userId) {
        final User user = userRepository.getOneById(userId);
        final List<ReviewComment> comments = reviewCommentRepository.findByUserId(user.getId());

        final List<KoiHistory> koiHistories = koiHistoryRepository.findKoiHistoryInCommentIds(
                comments.stream()
                        .map(ReviewComment::getId)
                        .collect(Collectors.toList())
        );

        final List<Long> commentIds = extractCommentIds(comments);
        final List<CommentLike> commentLikes = commentLikeRepository.findByCommentIdIn(commentIds);

        return UserCodeCommentDto.listOf(user, comments, koiHistories, commentLikes);
    }

    public List<CommentReviewDetailDto> getCommentsOnReview(Long sessionUserId, Long reviewId) {
        final CodeReview codeReview = codeReviewRepository.getOneById(reviewId);
        final List<ReviewComment> comments = reviewCommentRepository.findByCodeReviewId(codeReview.getId());

        final List<Long> commentIds = extractCommentIds(comments);
        final List<CommentLike> commentLikes = commentLikeRepository.findByCommentIdIn(commentIds);

        final List<KoiHistory> koiHistories = koiHistoryRepository.findKoiHistoryInCommentIds(commentIds);

        return CommentReviewDetailDto.listOf(comments, koiHistories, sessionUserId, commentLikes);
    }

    public List<HotReviewComment> getHotComments(Long sessionUserId) {
        final List<ReviewComment> comments = reviewCommentRepository.hotCommentRank();
        final List<Long> commentIds = extractCommentIds(comments);

        final List<KoiHistory> koiHistories = koiHistoryRepository.findKoiHistoryInCommentIds(commentIds);
        final List<CommentLike> commentLikes = commentLikeRepository.findByUserIdAndCommentIdIn(sessionUserId, commentIds);

        return HotReviewComment.listOf(comments, koiHistories, commentLikes);
    }

    private static List<Long> extractCommentIds(List<ReviewComment> comments) {
        return comments
                .stream()
                .map(ReviewComment::getId)
                .collect(Collectors.toList());
    }
}
