package codekoi.apiserver.domain.user.dto.response;

import codekoi.apiserver.domain.code.review.dto.UserSkillStatistics;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserStatisticsResponse {
    private List<UserSkillStatistics> skills = new ArrayList<>();

    public UserStatisticsResponse(List<UserSkillStatistics> skills) {
        this.skills = skills;
    }

    public static UserStatisticsResponse from(List<UserSkillStatistics> skills) {
        return new UserStatisticsResponse(skills);
    }
}
