package com.codekoi.fixture;


import com.codekoi.domain.skill.skill.Skill;

public enum SkillFixture {
    SPRING("스프링", 10),
    JPA("JPA",9),
    JAVA("Java",8),

    ;
    public final String name;
    public final int searchCount;

    SkillFixture(String name, int searchCount) {
        this.name = name;
        this.searchCount = searchCount;
    }

    public Skill toSkill() {
        return Skill.builder()
                .name(name)
                .searchCount(searchCount)
                .build();
    }

    public Skill toSkill(Long id) {
        return Skill.builder()
                .id(id)
                .name(name)
                .searchCount(searchCount)
                .build();
    }
}
