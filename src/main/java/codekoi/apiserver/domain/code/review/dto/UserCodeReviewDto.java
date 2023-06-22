package codekoi.apiserver.domain.code.review.dto;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.CodeReviewSkill;
import codekoi.apiserver.domain.code.review.domain.CodeReviewStatus;
import codekoi.apiserver.domain.code.review.domain.Favorite;
import codekoi.apiserver.domain.user.dto.UserProfileDto;
import codekoi.apiserver.global.util.time.BeforeTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserCodeReviewDto {

    private UserProfileDto user;

    @JsonSerialize(using = BeforeTimeSerializer.class)
    private LocalDateTime createdAt;
    private String title;
    private List<String> skills = new ArrayList<>();
    private CodeReviewStatus status;

    private Long reviewId;

    private Boolean isFavorite;

    public UserCodeReviewDto(UserProfileDto user, LocalDateTime createdAt, String title, List<String> skills, CodeReviewStatus status, Long reviewId, boolean isFavorite) {
        this.user = user;
        this.createdAt = createdAt;
        this.title = title;
        this.skills = skills;
        this.status = status;
        this.reviewId = reviewId;
        this.isFavorite = isFavorite;
    }

    public static List<UserCodeReviewDto> listOf(List<CodeReview> reviews, List<Favorite> favorites, boolean me) {
        final Map<Long, Favorite> favoriteMap = favorites.stream()
                .collect(Collectors.toMap(f -> f.getCodeReview().getId(), favorite -> favorite));

        return reviews
                .stream()
                .map(r -> new UserCodeReviewDto(UserProfileDto.from(r.getUser()), r.getCreatedAt(), r.getTitle(),
                        getSkillNames(r.getSkills()), r.getStatus(), r.getId(), me && favoriteMap.containsKey(r.getId())))
                .collect(Collectors.toList());
    }

    private static List<String> getSkillNames(List<CodeReviewSkill> skills) {
        return skills
                .stream()
                .map(s -> s.getSkill().getName())
                .toList();
    }

    public static List<UserCodeReviewDto> listOf(List<Favorite> favorites, boolean me) {
        return favorites.stream()
                .map(Favorite::getCodeReview)
                .map(r -> new UserCodeReviewDto(UserProfileDto.from(r.getUser()), r.getCreatedAt(), r.getTitle(), getSkillNames(r.getSkills()),
                        r.getStatus(), r.getId(), me))
                .collect(Collectors.toList());
    }
}
