package codekoi.apiserver.domain.skill.skill;

import jakarta.persistence.*;

@DiscriminatorValue("hardSkill")
@Entity
public class HardSkill extends Skill {

    private Integer searchCount;
}
