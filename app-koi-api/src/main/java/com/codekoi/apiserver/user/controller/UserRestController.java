package com.codekoi.apiserver.user.controller;

import com.codekoi.apiserver.comment.dto.UserCodeCommentDto;
import com.codekoi.apiserver.comment.service.ReviewCommentQueryService;
import com.codekoi.apiserver.review.controller.response.UserCommentedReviewListResponse;
import com.codekoi.apiserver.review.controller.response.UserDetailCodeReviewListResponse;
import com.codekoi.apiserver.review.dto.UserActivityHistory;
import com.codekoi.apiserver.review.dto.UserCodeReviewDto;
import com.codekoi.apiserver.review.service.CodeReviewQueryService;
import com.codekoi.apiserver.skill.review.dto.UserSkillStatistics;
import com.codekoi.apiserver.skill.review.service.CodeReviewSkillQueryService;
import com.codekoi.apiserver.user.controller.response.UserHistoryLogResponse;
import com.codekoi.apiserver.user.controller.response.UserStatisticsResponse;
import com.codekoi.apiserver.user.dto.UserDetail;
import com.codekoi.apiserver.user.service.UserQueryService;
import com.codekoi.coreweb.jwt.AuthInfo;
import com.codekoi.coreweb.jwt.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserQueryService userQueryService;
    private final CodeReviewQueryService codeReviewQueryService;
    private final CodeReviewSkillQueryService codeReviewSkillQueryService;
    private final ReviewCommentQueryService reviewCommentQueryService;

    @GetMapping("/{userId}")
    public UserDetail getUserDetail(@Principal AuthInfo sessionUser, @PathVariable Long userId) {
        return userQueryService.getUserDetail(sessionUser.getUserId(), userId);
    }

    @GetMapping("/{userId}/logs")
    public UserHistoryLogResponse getUserHistoryLog(@PathVariable Long userId) {
        final List<UserActivityHistory> userHistory = codeReviewQueryService.findUserHistory(userId);
        return new UserHistoryLogResponse(userHistory);
    }

    @GetMapping("/{userId}/comments")
    public UserCommentedReviewListResponse userCommentedReviews(@PathVariable Long userId) {
        final List<UserCodeCommentDto> userComments = reviewCommentQueryService.getUserComments(userId);
        return new UserCommentedReviewListResponse(userComments);
    }

    @GetMapping("/{userId}/reviews")
    public UserDetailCodeReviewListResponse requestedReviews(@Principal AuthInfo authInfo, @PathVariable Long userId) {
        final List<UserCodeReviewDto> codeReviewList = codeReviewQueryService.findRequestedCodeReviews(authInfo.getUserId(), userId);
        return new UserDetailCodeReviewListResponse(codeReviewList);
    }

    @GetMapping("/{userId}/favorite/reviews")
    public UserDetailCodeReviewListResponse favoriteReviews(@Principal AuthInfo userToken, @PathVariable Long userId) {
        final List<UserCodeReviewDto> favoriteCodeReviews = codeReviewQueryService.findFavoriteCodeReviews(userToken.getUserId(), userId);
        return new UserDetailCodeReviewListResponse(favoriteCodeReviews);
    }

    @GetMapping("/{userId}/skills/statistics")
    public UserStatisticsResponse userSkillStatistics(@PathVariable Long userId) {
        final List<UserSkillStatistics> skillStatistics = codeReviewSkillQueryService.findUserSkillStatistics(userId);
        return new UserStatisticsResponse(skillStatistics);
    }
}
