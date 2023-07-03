package codekoi.apiserver.utils.fixture;

import codekoi.apiserver.domain.skill.doamin.Skill;

public enum SkillFixture {
    SPRING("스프링"),
    JPA("JPA"),
    JAVA("Java"),

    ;
    public final String name;

    SkillFixture(String name) {
        this.name = name;
    }

    public Skill toHardSkill() {
        return Skill.builder()
                .name(name)
                .build();
    }
}
