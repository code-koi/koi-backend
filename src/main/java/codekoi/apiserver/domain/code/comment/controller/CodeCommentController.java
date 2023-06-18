package codekoi.apiserver.domain.code.comment.controller;

import codekoi.apiserver.domain.code.comment.service.CodeCommentQuery;
import codekoi.apiserver.domain.code.review.controller.dto.response.UserCommentedReviewListResponse;
import codekoi.apiserver.domain.code.review.dto.UserCodeCommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/code-comments")
@RequiredArgsConstructor
public class CodeCommentController {

    private final CodeCommentQuery codeCommentQuery;

    @GetMapping
    public UserCommentedReviewListResponse commentedReviews(@RequestParam Long userId) {
        final List<UserCodeCommentDto> userComments = codeCommentQuery.getUserComments(userId);
        return UserCommentedReviewListResponse.from(userComments);

    }
}
