package codekoi.apiserver.domain.code.review.controller;

import codekoi.apiserver.domain.code.review.dto.response.UserDetailCodeReviewListResponse;
import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import codekoi.apiserver.domain.code.review.service.CodeReviewQuery;
import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.global.token.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/code-reviews")
@RequiredArgsConstructor
public class CodeReviewController {

    private final CodeReviewQuery codeReviewQuery;

    @GetMapping
    public UserDetailCodeReviewListResponse requestedReviews(@Principal UserToken userToken, @RequestParam Long userId) {
        final List<UserCodeReviewDto> codeReviewList = codeReviewQuery.findRequestedCodeReviews(userToken.getUserId(), userId);
        return UserDetailCodeReviewListResponse.from(codeReviewList);
    }

    @GetMapping("/favorite")
    public UserDetailCodeReviewListResponse favoriteReviews(@Principal UserToken userToken, @RequestParam Long userId) {
        final List<UserCodeReviewDto> favoriteCodeReviews = codeReviewQuery.findFavoriteCodeReviews(userToken.getUserId(), userId);
        return UserDetailCodeReviewListResponse.from(favoriteCodeReviews);
    }
}
