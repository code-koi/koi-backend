package com.codekoi.apiserver.review.service;

import com.codekoi.apiserver.comment.repository.ReviewCommentRepository;
import com.codekoi.apiserver.favorite.repository.FavoriteRepository;
import com.codekoi.apiserver.like.repository.LikeRepository;
import com.codekoi.apiserver.review.dto.CodeReviewDetailDto;
import com.codekoi.apiserver.review.dto.HotCodeReview;
import com.codekoi.apiserver.review.dto.UserActivityHistory;
import com.codekoi.apiserver.review.dto.UserCodeReviewDto;
import com.codekoi.apiserver.review.repository.CodeReviewRepository;
import com.codekoi.apiserver.review.vo.Activity;
import com.codekoi.apiserver.review.vo.ActivityHistories;
import com.codekoi.domain.comment.entity.ReviewComment;
import com.codekoi.domain.favorite.entity.Favorite;
import com.codekoi.domain.like.entity.Like;
import com.codekoi.domain.review.entity.CodeReview;
import com.codekoi.domain.review.repository.CodeReviewCoreRepository;
import com.codekoi.domain.user.entity.User;
import com.codekoi.domain.user.repository.UserCoreRepository;
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

    private final CodeReviewCoreRepository codeReviewCoreRepository;
    private final UserCoreRepository userCoreRepository;


    public List<UserCodeReviewDto> findRequestedCodeReviews(Long sessionUserId, Long userId) {
        final User user = userCoreRepository.getOneById(userId);
        final List<CodeReview> reviews = codeReviewRepository.findByUserId(userId);

        final List<Long> reviewIds = reviews.stream()
                .map(CodeReview::getId)
                .toList();

        final List<Favorite> favorites = favoriteRepository.findByUserIdAndCodeReviewIdIn(userId, reviewIds);

        return UserCodeReviewDto.listOf(reviews, favorites, sessionUserId.equals(userId));
    }

    public List<UserCodeReviewDto> findFavoriteCodeReviews(Long sessionUserId, Long userId) {
        final User user = userCoreRepository.getOneById(userId);
        final List<Favorite> favorites = favoriteRepository.findAllByUserId(user.getId());

        return UserCodeReviewDto.listOf(favorites, sessionUserId.equals(userId));
    }

    public CodeReviewDetailDto findCodeReviewDetail(Long sessionUserId, Long codeReviewId) {
        final CodeReview codeReview = codeReviewCoreRepository.getOneById(codeReviewId);

        final User reviewRequestUser = codeReview.getUser();
        final Optional<Favorite> optionalFavorite = favoriteRepository.findByUserId(reviewRequestUser.getId());

        return CodeReviewDetailDto.of(codeReview, optionalFavorite.isPresent(),
                sessionUserId.equals(reviewRequestUser.getId()));
    }

    public List<UserActivityHistory> findUserHistory(Long userId) {
        final User user = userCoreRepository.getOneById(userId);

        final List<CodeReview> codeReviews = codeReviewRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        final List<ReviewComment> comments = reviewCommentRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        final List<Like> likes = likeRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        final List<Favorite> favorites = favoriteRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);

        final ActivityHistories histories = new ActivityHistories(codeReviews, comments, likes, favorites);
        final List<Activity> top = histories.getTopN();

        return UserActivityHistory.listFrom(top);
    }

    public List<HotCodeReview> getHotReviews() {
        final List<Long> hotReviewIds = favoriteRepository.hotReviewRank();
        final List<CodeReview> reviews = codeReviewRepository.findAllById(hotReviewIds);

        return HotCodeReview.listFrom(reviews);
    }
}
