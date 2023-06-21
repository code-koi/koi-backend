package codekoi.apiserver.domain.code.review.dto.response;

import codekoi.apiserver.domain.code.review.dto.UserCodeCommentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserCommentedReviewListResponse {

    private List<UserCodeCommentDto> comments;

    public UserCommentedReviewListResponse(List<UserCodeCommentDto> comments) {
        this.comments = comments;
    }

    public static UserCommentedReviewListResponse from(List<UserCodeCommentDto> comments) {
        return new UserCommentedReviewListResponse(comments);
    }
}
