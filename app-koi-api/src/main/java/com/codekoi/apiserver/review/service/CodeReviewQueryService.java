package com.codekoi.apiserver.review.service;

import com.codekoi.apiserver.comment.repository.ReviewCommentQueryRepository;
import com.codekoi.apiserver.favorite.repository.FavoriteQueryQueryRepository;
import com.codekoi.apiserver.like.repository.LikeQueryRepository;
import com.codekoi.apiserver.review.dto.CodeReviewDetailDto;
import com.codekoi.apiserver.review.dto.HotCodeReview;
import com.codekoi.apiserver.review.dto.UserActivityHistory;
import com.codekoi.apiserver.review.dto.UserCodeReviewDto;
import com.codekoi.apiserver.review.repository.CodeReviewQueryRepository;
import com.codekoi.apiserver.review.vo.Activity;
import com.codekoi.apiserver.review.vo.ActivityHistories;
import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.favorite.Favorite;
import com.codekoi.domain.like.Like;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CodeReviewQueryService {

    private final FavoriteQueryQueryRepository favoriteQueryRepository;
    private final CodeReviewQueryRepository codeReviewQueryRepository;
    private final ReviewCommentQueryRepository reviewCommentQueryRepository;
    private final LikeQueryRepository likeQueryRepository;

    private final CodeReviewRepository codeReviewRepository;
    private final UserRepository userRepository;


    public List<UserCodeReviewDto> findRequestedCodeReviews(Long sessionUserId, Long userId) {
        final User user = userRepository.getOneById(userId);
        final List<CodeReview> reviews = codeReviewQueryRepository.findByUserId(userId);

        final List<Long> reviewIds = reviews.stream()
                .map(CodeReview::getId)
                .toList();

        final List<Favorite> favorites = favoriteQueryRepository.findByUserIdAndCodeReviewIdIn(userId, reviewIds);

        return UserCodeReviewDto.listOf(reviews, favorites, sessionUserId.equals(userId));
    }

    public List<UserCodeReviewDto> findFavoriteCodeReviews(Long sessionUserId, Long userId) {
        final User user = userRepository.getOneById(userId);
        final List<Favorite> favorites = favoriteQueryRepository.findAllByUserId(user.getId());

        return UserCodeReviewDto.listOf(favorites, sessionUserId.equals(userId));
    }

    public CodeReviewDetailDto findCodeReviewDetail(Long sessionUserId, Long codeReviewId) {
        final CodeReview codeReview = codeReviewQueryRepository.getOneById(codeReviewId);

        final User reviewRequestUser = codeReview.getUser();
        final Optional<Favorite> optionalFavorite = favoriteQueryRepository.findByUserId(reviewRequestUser.getId());

        return CodeReviewDetailDto.of(codeReview, optionalFavorite.isPresent(),
                sessionUserId.equals(reviewRequestUser.getId()));
    }

    public List<UserActivityHistory> findUserHistory(Long userId) {
        final User user = userRepository.getOneById(userId);

        final List<CodeReview> codeReviews = codeReviewQueryRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        final List<ReviewComment> comments = reviewCommentQueryRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        final List<Like> likes = likeQueryRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
        final List<Favorite> favorites = favoriteQueryRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);

        final ActivityHistories histories = new ActivityHistories(codeReviews, comments, likes, favorites);
        final List<Activity> top = histories.getTopN();

        return UserActivityHistory.listFrom(top);
    }

    public List<HotCodeReview> getHotReviews() {
        final List<Long> hotReviewIds = favoriteQueryRepository.hotReviewRank();
        final List<CodeReview> reviews = codeReviewQueryRepository.findAllById(hotReviewIds);

        return HotCodeReview.listFrom(reviews);
    }
}
