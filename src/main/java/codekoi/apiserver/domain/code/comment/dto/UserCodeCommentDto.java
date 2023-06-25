package codekoi.apiserver.domain.code.comment.dto;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.koi.domain.KoiType;
import codekoi.apiserver.domain.koi.history.domain.KoiHistory;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.dto.UserProfileDto;
import codekoi.apiserver.global.util.time.BeforeTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserCodeCommentDto {

    private UserProfileDto user;

    private Long reviewId;

    @JsonSerialize(using = BeforeTimeSerializer.class)
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

    public static List<UserCodeCommentDto> listOf(User user, List<CodeReviewComment> comments, List<KoiHistory> koiHistories) {
        final Map<Long, KoiType> koiMap = getKoiMap(koiHistories);

        return comments.stream()
                .map(c -> UserCodeCommentDto.of(user, c, koiMap.get(c.getId())))
                .collect(Collectors.toList());
    }

    public static List<UserCodeCommentDto> listOf(List<CodeReviewComment> comment, List<KoiHistory> koiHistories) {
        final Map<Long, KoiType> koiMap = getKoiMap(koiHistories);

        return comment.stream()
                .map(c -> UserCodeCommentDto.of(c.getUser(), c, koiMap.get(c.getId())))
                .collect(Collectors.toList());
    }

    private static Map<Long, KoiType> getKoiMap(List<KoiHistory> koiHistories) {
        return koiHistories.stream()
                .collect(Collectors.toMap(koiHistory -> koiHistory.getCodeReviewComment().getId(),
                        KoiHistory::getKoiType));
    }
}
