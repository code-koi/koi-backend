package com.codekoi.apiserver.skill.review.dto;

import com.codekoi.skill.Skill;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSkillStatistics {

    private Long id;

    private String name;

    private int count;

    public UserSkillStatistics(Long id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public static UserSkillStatistics of(Skill skill, int count) {
        return new UserSkillStatistics(skill.getId(), skill.getName(), count);
    }
}
