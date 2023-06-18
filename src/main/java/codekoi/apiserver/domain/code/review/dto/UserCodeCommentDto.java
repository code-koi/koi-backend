package codekoi.apiserver.domain.code.review.dto;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.koi.domain.KoiType;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.dto.UserProfileDto;
import codekoi.apiserver.global.util.time.TimePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserCodeCommentDto {

    private UserProfileDto user;

    private Long reviewId;

    @JsonFormat(pattern = TimePattern.BASIC_FORMAT_STRING)
    private LocalDateTime createdAt;

    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private KoiType koiType;

    public UserCodeCommentDto(UserProfileDto user, Long reviewId, LocalDateTime createdAt, String content, KoiType koiType) {
        this.user = user;
        this.reviewId = reviewId;
        this.createdAt = createdAt;
        this.content = content;
        this.koiType = koiType;
    }

    public static UserCodeCommentDto of(User user, CodeReviewComment comment, KoiType koiType) {
        final UserProfileDto userDto = UserProfileDto.from(user);
        return new UserCodeCommentDto(userDto, comment.getCodeReview().getId(), comment.getCreatedAt(), comment.getContent(),
                koiType);
    }
}
