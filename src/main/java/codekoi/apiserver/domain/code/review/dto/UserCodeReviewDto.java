package codekoi.apiserver.domain.code.review.dto;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.CodeReviewStatus;
import codekoi.apiserver.domain.user.dto.UserProfileDto;
import codekoi.apiserver.global.util.time.BeforeTimeSerializer;
import codekoi.apiserver.global.util.time.TimePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserCodeReviewDto {

    private UserProfileDto user;

    @JsonSerialize(using = BeforeTimeSerializer.class)
    private LocalDateTime createdAt;
    private String title;
    private List<String> skills = new ArrayList<>();
    private CodeReviewStatus status;

    public UserCodeReviewDto(UserProfileDto user, LocalDateTime createdAt, String title, List<String> skills, CodeReviewStatus status) {
        this.user = user;
        this.createdAt = createdAt;
        this.title = title;
        this.skills = skills;
        this.status = status;
    }

    public static UserCodeReviewDto from(CodeReview codeReview) {
        final List<String> skillNames = codeReview.getSkills()
                .stream()
                .map(s -> s.getSkill().getName())
                .toList();

        final UserProfileDto profileDto = UserProfileDto.from(codeReview.getUser());

        return new UserCodeReviewDto(profileDto, codeReview.getCreatedAt(), codeReview.getTitle(), skillNames,
                codeReview.getStatus());
    }
}
