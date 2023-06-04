package codekoi.apiserver.domain.skill.skill;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@DiscriminatorValue("softSkill")
@Entity
public class SoftSkill extends Skill {

    @Enumerated(EnumType.STRING)
    private MentoringType mentoringType;

}
