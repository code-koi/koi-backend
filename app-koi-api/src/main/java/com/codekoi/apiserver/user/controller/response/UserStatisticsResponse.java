package com.codekoi.apiserver.user.controller.response;

import com.codekoi.apiserver.skill.review.dto.UserSkillStatistics;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserStatisticsResponse {
    private List<UserSkillStatistics> skills = new ArrayList<>();

    public UserStatisticsResponse(List<UserSkillStatistics> skills) {
        this.skills = skills;
    }

}
