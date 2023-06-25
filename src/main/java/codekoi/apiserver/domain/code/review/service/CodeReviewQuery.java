package codekoi.apiserver.domain.code.review.service;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.Favorite;
import codekoi.apiserver.domain.code.review.dto.CodeReviewDetailDto;
import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import codekoi.apiserver.domain.code.review.exception.CodeReviewNotFoundException;
import codekoi.apiserver.domain.code.review.repository.CodeFavoriteRepository;
import codekoi.apiserver.domain.code.review.repository.CodeReviewRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeReviewQuery {

    private final CodeReviewRepository codeReviewRepository;
    private final UserRepository userRepository;

    private final CodeFavoriteRepository favoriteRepository;

    public List<UserCodeReviewDto> findRequestedCodeReviews(Long sessionUserId, Long userId) {
        final User user = userRepository.findByUserId(userId);
        final List<CodeReview> reviews = codeReviewRepository.findByUser(user);

        final List<Favorite> favorites = favoriteRepository.findFavoriteByUserAndCodeReviewIn(user, reviews);

        return UserCodeReviewDto.listOf(reviews, favorites, sessionUserId.equals(userId));
    }

    public List<UserCodeReviewDto> findFavoriteCodeReviews(Long sessionUserId, Long userId) {
        final User user = userRepository.findByUserId(userId);
        final List<Favorite> favorites = favoriteRepository.findFavoritesByUser(user);

        return UserCodeReviewDto.listOf(favorites, sessionUserId.equals(userId));
    }

    public CodeReviewDetailDto findCodeReviewDetail(Long sessionUserId, Long codeReviewId) {
        final CodeReview codeReview = codeReviewRepository.findByCodeReviewIdWithUser(codeReviewId)
                .orElseThrow(CodeReviewNotFoundException::new);

        final User reviewRequestUser = codeReview.getUser();
        final Optional<Favorite> optionalFavorite = favoriteRepository.findByUser(reviewRequestUser);

        return CodeReviewDetailDto.of(codeReview, optionalFavorite.isPresent(),
                sessionUserId.equals(reviewRequestUser.getId()));
    }
}
