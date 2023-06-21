package codekoi.apiserver.domain.code.review.service;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.Favorite;
import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import codekoi.apiserver.domain.code.review.repository.CodeFavoriteRepository;
import codekoi.apiserver.domain.code.review.repository.CodeReviewRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeReviewQuery {

    private final CodeReviewRepository codeReviewRepository;
    private final UserRepository userRepository;

    private final CodeFavoriteRepository favoriteRepository;

    public List<UserCodeReviewDto> findRequestedCodeReviews(Long userId) {
        final User user = userRepository.findUserById(userId);
        final List<CodeReview> reviews = codeReviewRepository.findByUser(user);

        return reviews.stream()
                .map(UserCodeReviewDto::from)
                .collect(Collectors.toList());
    }

    public List<UserCodeReviewDto> findFavoriteCodeReviews(Long userId) {
        final User user = userRepository.findUserById(userId);
        final List<Favorite> favorites = favoriteRepository.findFavoriteByUser(user);

        return favorites.stream()
                .map(Favorite::getCodeReview)
                .map(UserCodeReviewDto::from)
                .collect(Collectors.toList());
    }
}
