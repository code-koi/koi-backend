package com.codekoi.apiserver.skill.skill.controller.response;

import com.codekoi.apiserver.skill.skill.dto.SkillInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class TopSkillRankResponse {
    private List<SkillInfo> skills;

    public TopSkillRankResponse(List<SkillInfo> skills) {
        this.skills = skills;
    }
}
