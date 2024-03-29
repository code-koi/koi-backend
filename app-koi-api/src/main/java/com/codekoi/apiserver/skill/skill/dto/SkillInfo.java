package com.codekoi.apiserver.skill.skill.dto;

import com.codekoi.skill.Skill;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class SkillInfo {

    private String name;

    private Long id;

    public SkillInfo(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    private static SkillInfo from(Skill skill) {
        return new SkillInfo(skill.getName(), skill.getId());
    }

    public static List<SkillInfo> listFrom(List<Skill> hardSkills) {
        return hardSkills.stream()
                .map(SkillInfo::from)
                .toList();
    }
}
