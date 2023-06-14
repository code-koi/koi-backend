package codekoi.apiserver.utils.fixture;

import codekoi.apiserver.domain.skill.doamin.HardSkill;

public enum HardSkillFixture {
    SPRING("스프링"),
    JPA("JPA"),
    JAVA("Java"),

    ;
    public final String name;

    HardSkillFixture(String name) {
        this.name = name;
    }

    public HardSkill toHardSkill() {
        return HardSkill.builder()
                .name(name)
                .build();
    }
}
