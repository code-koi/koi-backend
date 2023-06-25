package codekoi.apiserver.domain.code.comment.dto;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.koi.domain.KoiType;
import codekoi.apiserver.domain.koi.history.domain.KoiHistory;
import codekoi.apiserver.domain.user.dto.UserProfileDto;
import codekoi.apiserver.global.util.time.BeforeTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CodeCommentDetailDto {

    private UserProfileDto user;

    private Long id;

    @JsonSerialize(using = BeforeTimeSerializer.class)
    private LocalDateTime createdAt;

    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private KoiType koiType;

    private Boolean me;

    public CodeCommentDetailDto(UserProfileDto user, Long id, LocalDateTime createdAt, String content, KoiType koiType, Boolean me) {
        this.user = user;
        this.id = id;
        this.createdAt = createdAt;
        this.content = content;
        this.koiType = koiType;
        this.me = me;
    }

    public static List<CodeCommentDetailDto> listOf(List<CodeReviewComment> comment, List<KoiHistory> koiHistories, Long sessionUserId) {
        final Map<Long, KoiType> koiMap = getKoiMap(koiHistories);
        return comment.stream()
                .map(c -> new CodeCommentDetailDto(UserProfileDto.from(c.getUser()), c.getId(), c.getCreatedAt(),
                        c.getContent(), koiMap.get(c.getId()), Objects.equals(sessionUserId, c.getUser().getId())))
                .collect(Collectors.toList());

    }

    private static Map<Long, KoiType> getKoiMap(List<KoiHistory> koiHistories) {
        return koiHistories.stream()
                .collect(Collectors.toMap(koiHistory -> koiHistory.getCodeReviewComment().getId(),
                        KoiHistory::getKoiType));
    }

}
