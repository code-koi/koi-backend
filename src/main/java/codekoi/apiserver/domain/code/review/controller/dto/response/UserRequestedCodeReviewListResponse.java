package codekoi.apiserver.domain.code.review.controller.dto.response;

import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class UserRequestedCodeReviewListResponse {

    private List<UserCodeReviewDto> reviews = new ArrayList<>();

    private UserRequestedCodeReviewListResponse(List<UserCodeReviewDto> reviews) {
        this.reviews = reviews;
    }

    public static UserRequestedCodeReviewListResponse from(List<UserCodeReviewDto> reviews) {
        return new UserRequestedCodeReviewListResponse(reviews);
    }
}
