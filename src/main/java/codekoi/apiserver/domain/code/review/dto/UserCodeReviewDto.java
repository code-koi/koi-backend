package codekoi.apiserver.domain.code.review.dto;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.CodeReviewStatus;
import codekoi.apiserver.domain.user.dto.UserProfileDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserCodeReviewDto {

    private UserProfileDto user;

    private String createdAt;
    private String title;
    private List<String> skills = new ArrayList<>();
    private CodeReviewStatus status;

    public UserCodeReviewDto(UserProfileDto user, String createdAt, String title, List<String> skills, CodeReviewStatus status) {
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

        return new UserCodeReviewDto(profileDto, codeReview.getCreatedAt().toString(), codeReview.getTitle(), skillNames,
                codeReview.getStatus());
    }
}
