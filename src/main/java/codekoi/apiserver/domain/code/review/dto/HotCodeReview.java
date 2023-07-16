package codekoi.apiserver.domain.code.review.dto;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.CodeReviewStatus;
import codekoi.apiserver.domain.user.dto.UserProfileDto;
import codekoi.apiserver.global.util.time.BeforeTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class HotCodeReview {
    private Long id;
    private String title;

    private CodeReviewStatus status;

    @JsonSerialize(using = BeforeTimeSerializer.class)
    private LocalDateTime createdAt;

    private UserProfileDto user;

    private List<String> skills;

    public HotCodeReview(Long id, String title, CodeReviewStatus status, LocalDateTime createdAt, UserProfileDto user, List<String> skills) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
        this.user = user;
        this.skills = skills;
    }

    public static List<HotCodeReview> listFrom(List<CodeReview> reviews) {
        return reviews.stream()
                .map(r -> new HotCodeReview(
                        r.getId(), r.getTitle(), r.getStatus(), r.getCreatedAt(),
                        UserProfileDto.from(r.getUser()), r.getSkillNames()
                )).collect(Collectors.toList());
    }
}
