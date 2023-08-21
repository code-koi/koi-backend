package com.codekoi.apiserver.review.service;

import com.codekoi.apiserver.review.dto.*;
import com.codekoi.apiserver.review.repository.CodeReviewQueryRepository;
import com.codekoi.apiserver.review.vo.Activity;
import com.codekoi.apiserver.review.vo.ActivityHistories;
import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.comment.ReviewCommentRepository;
import com.codekoi.domain.favorite.Favorite;
import com.codekoi.domain.favorite.FavoriteRepository;
import com.codekoi.domain.like.Like;
import com.codekoi.domain.like.LikeRepository;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
import com.codekoi.pagination.NoOffSetPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CodeReviewQueryService {

    private final FavoriteRepository favoriteRepository;
    private final CodeReviewRepository codeReviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    private final CodeReviewQueryRepository codeReviewQueryRepository;

    public NoOffSetPagination<BasicCodeReview, Long> getReviewList(CodeReviewListCondition condition) {
        int pageSize = 24;
        final List<CodeReview> reviews = codeReviewQueryRepository.getReviewList(condition, pageSize);

        final List<BasicCodeReview> reviewDtoList = BasicCodeReview.listFrom(reviews);
        return new NoOffSetPagination<>(reviewDtoList, pageSize, BasicCodeReview::getId);
    }

    public List<UserCodeReviewDto> findRequestedCodeReviews(Long sessionUserId, Long userId) {
        final User user = userRepository.getOneById(userId);
        final List<CodeReview> reviews = codeReviewRepository.findByUserId(userId);

        final List<Long> reviewIds = reviews.stream()
                .map(CodeReview::getId)
                .toList();

        final List<Favorite> favorites = favoriteRepository.findByUserIdAndCodeReviewIdIn(userId, reviewIds);

        return UserCodeReviewDto.listOf(reviews, favorites, sessionUserId.equals(userId));
    }

    public List<UserCodeReviewDto> findFavoriteCodeReviews(Long sessionUserId, Long userId) {
        final User user = userRepository.getOneById(userId);
        final List<Favorite> favorites = favoriteRepository.findAllByUserId(user.getId());

        return UserCodeReviewDto.listOf(favorites, sessionUserId.equals(userId));
    }

    public CodeReviewDetailDto findCodeReviewDetail(Long sessionUserId, Long codeReviewId) {
        final CodeReview codeReview = codeReviewRepository.getOneById(codeReviewId);

        final User reviewRequestUser = codeReview.getUser();
        final Optional<Favorite> optionalFavorite = favoriteRepository.findByUserIdAndCodeReviewId(reviewRequestUser.getId(), codeReviewId);

        return CodeReviewDetailDto.of(codeReview, optionalFavorite.isPresent(),
                sessionUserId.equals(reviewRequestUser.getId()));
    }

    public List<UserActivityHistory> findUserHistory(Long userId) {
        final User user = userRepository.getOneById(userId);

        final List<CodeReview> codeReviews = codeReviewRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        final List<ReviewComment> comments = reviewCommentRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        final List<Like> likes = likeRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        final List<Favorite> favorites = favoriteRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);

        final ActivityHistories histories = new ActivityHistories(codeReviews, comments, likes, favorites);
        final List<Activity> top = histories.getTopN();

        return UserActivityHistory.listFrom(top);
    }

    public List<BasicCodeReview> getHotReviews() {
        final List<Long> hotReviewIds = favoriteRepository.hotReviewRank();
        final List<CodeReview> reviews = codeReviewRepository.findAllById(hotReviewIds);

        return BasicCodeReview.listFrom(reviews);
    }
}
