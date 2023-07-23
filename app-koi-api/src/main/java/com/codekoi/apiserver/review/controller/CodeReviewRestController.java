package com.codekoi.apiserver.review.controller;

import com.codekoi.apiserver.comment.dto.CommentReviewDetailDto;
import com.codekoi.apiserver.comment.service.ReviewCommentQueryService;
import com.codekoi.apiserver.review.controller.response.CodeReviewDetailResponse;
import com.codekoi.apiserver.review.controller.response.HotCodeReviewListResponse;
import com.codekoi.apiserver.review.controller.response.ReviewCommentListResponse;
import com.codekoi.apiserver.review.dto.CodeReviewDetailDto;
import com.codekoi.apiserver.review.dto.HotCodeReview;
import com.codekoi.apiserver.review.service.CodeReviewQueryService;
import com.codekoi.coreweb.jwt.AuthInfo;
import com.codekoi.coreweb.jwt.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/code-reviews")
@RequiredArgsConstructor
public class CodeReviewRestController {

    private final CodeReviewQueryService codeReviewQueryService;
    private final ReviewCommentQueryService reviewCommentQueryService;

    @GetMapping("/hot")
    public HotCodeReviewListResponse hotReviews() {
        final List<HotCodeReview> hotReviews = codeReviewQueryService.getHotReviews();
        return new HotCodeReviewListResponse(hotReviews);
    }

    @GetMapping("/{reviewId}")
    public CodeReviewDetailResponse reviewDetail(@Principal AuthInfo authInfo, @PathVariable Long reviewId) {
        final CodeReviewDetailDto codeReviewDetail = codeReviewQueryService.findCodeReviewDetail(authInfo.getUserId(), reviewId);
        return new CodeReviewDetailResponse(codeReviewDetail);
    }

    @GetMapping("/{reviewId}/comments")
    public ReviewCommentListResponse commentsOnReview(@Principal AuthInfo authInfo, @PathVariable Long reviewId) {
        final List<CommentReviewDetailDto> comments = reviewCommentQueryService.getCommentsOnReview(authInfo.getUserId(), reviewId);
        return new ReviewCommentListResponse(comments);
    }
}
