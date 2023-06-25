package codekoi.apiserver.domain.code.review.controller;

import codekoi.apiserver.domain.code.review.dto.CodeReviewDetailDto;
import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import codekoi.apiserver.domain.code.review.dto.response.UserDetailCodeReviewListResponse;
import codekoi.apiserver.domain.code.review.service.CodeReviewQuery;
import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.global.token.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/{reviewId}")
    public ResponseEntity<Object> reviewDetail(@Principal UserToken userToken, @PathVariable Long reviewId) {
        final CodeReviewDetailDto codeReviewDetail = codeReviewQuery.findCodeReviewDetail(userToken.getUserId(), reviewId);
        return ResponseEntity.ok(Map.of("review", codeReviewDetail));
    }

    @GetMapping("/favorite")
    public UserDetailCodeReviewListResponse favoriteReviews(@Principal UserToken userToken, @RequestParam Long userId) {
        final List<UserCodeReviewDto> favoriteCodeReviews = codeReviewQuery.findFavoriteCodeReviews(userToken.getUserId(), userId);
        return UserDetailCodeReviewListResponse.from(favoriteCodeReviews);
    }
}
