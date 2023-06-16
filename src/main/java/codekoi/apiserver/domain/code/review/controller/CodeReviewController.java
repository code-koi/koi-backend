package codekoi.apiserver.domain.code.review.controller;

import codekoi.apiserver.domain.code.review.controller.dto.response.UserRequestedCodeReviewListResponse;
import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import codekoi.apiserver.domain.code.review.service.CodeReviewQuery;
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
    public UserRequestedCodeReviewListResponse requestedReviews(@RequestParam Long userId) {
        final List<UserCodeReviewDto> codeReviewList = codeReviewQuery.findRequestedCodeReviewList(userId);
        return UserRequestedCodeReviewListResponse.from(codeReviewList);
    }
}
