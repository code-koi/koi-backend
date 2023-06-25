package codekoi.apiserver.domain.code.review.controller;

import codekoi.apiserver.domain.code.comment.dto.CodeCommentDetailDto;
import codekoi.apiserver.domain.code.comment.service.CodeCommentQuery;
import codekoi.apiserver.domain.code.review.dto.CodeReviewDetailDto;
import codekoi.apiserver.domain.code.review.service.CodeReviewQuery;
import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.global.token.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/code-reviews")
@RequiredArgsConstructor
public class CodeReviewController {

    private final CodeReviewQuery codeReviewQuery;
    private final CodeCommentQuery commentQuery;


    @GetMapping("/{reviewId}")
    public ResponseEntity<Object> reviewDetail(@Principal UserToken userToken, @PathVariable Long reviewId) {
        final CodeReviewDetailDto codeReviewDetail = codeReviewQuery.findCodeReviewDetail(userToken.getUserId(), reviewId);
        return ResponseEntity.ok(Map.of("review", codeReviewDetail));
    }

    @GetMapping("/{reviewId}/comments")
    public ResponseEntity<Object> commentsOnReview(@Principal UserToken userToken, @PathVariable Long reviewId) {
        final List<CodeCommentDetailDto> comments = commentQuery.getCommentsOnReview(userToken.getUserId(), reviewId);
        return ResponseEntity.ok(Map.of("comments", comments));
    }
}
