package codekoi.apiserver.domain.user.controller;

import codekoi.apiserver.domain.code.comment.dto.UserCodeCommentDto;
import codekoi.apiserver.domain.code.comment.service.CodeCommentQuery;
import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import codekoi.apiserver.domain.code.review.dto.response.UserCommentedReviewListResponse;
import codekoi.apiserver.domain.code.review.dto.response.UserDetailCodeReviewListResponse;
import codekoi.apiserver.domain.code.review.service.CodeReviewQuery;
import codekoi.apiserver.domain.user.dto.UserDetail;
import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.domain.user.service.UserQuery;
import codekoi.apiserver.global.token.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserQuery userQuery;
    private final CodeCommentQuery commentQuery;
    private final CodeReviewQuery codeReviewQuery;

    @GetMapping("/{userId}")
    public UserDetail getUserDetail(@Principal UserToken sessionUser, @PathVariable Long userId) {
        return userQuery.gerUserDetail(sessionUser.getUserId(), userId);
    }

    @GetMapping("/{userId}/reviews")
    public UserDetailCodeReviewListResponse requestedReviews(@Principal UserToken userToken, @PathVariable Long userId) {
        final List<UserCodeReviewDto> codeReviewList = codeReviewQuery.findRequestedCodeReviews(userToken.getUserId(), userId);
        return UserDetailCodeReviewListResponse.from(codeReviewList);
    }

    @GetMapping("/{userId}/comments")
    public UserCommentedReviewListResponse userCommentedReviews(@PathVariable Long userId) {
        final List<UserCodeCommentDto> userComments = commentQuery.getUserComments(userId);
        return UserCommentedReviewListResponse.from(userComments);
    }

    @GetMapping("/{userId}/favorite")
    public UserDetailCodeReviewListResponse favoriteReviews(@Principal UserToken userToken, @PathVariable Long userId) {
        final List<UserCodeReviewDto> favoriteCodeReviews = codeReviewQuery.findFavoriteCodeReviews(userToken.getUserId(), userId);
        return UserDetailCodeReviewListResponse.from(favoriteCodeReviews);
    }
}
