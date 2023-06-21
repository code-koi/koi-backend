package codekoi.apiserver.domain.code.review.dto.response;

import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class UserDetailCodeReviewListResponse {

    private List<UserCodeReviewDto> reviews = new ArrayList<>();

    private UserDetailCodeReviewListResponse(List<UserCodeReviewDto> reviews) {
        this.reviews = reviews;
    }

    public static UserDetailCodeReviewListResponse from(List<UserCodeReviewDto> reviews) {
        return new UserDetailCodeReviewListResponse(reviews);
    }
}
