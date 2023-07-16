package codekoi.apiserver.domain.code.review.dto.response;

import codekoi.apiserver.domain.code.review.dto.HotCodeReview;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class HotCodeReviewListResponse {

    private List<HotCodeReview> reviews = new ArrayList<>();

    public HotCodeReviewListResponse(List<HotCodeReview> reviews) {
        this.reviews = reviews;
    }

    public static HotCodeReviewListResponse from(List<HotCodeReview> reviews) {
        return new HotCodeReviewListResponse(reviews);
    }
}
