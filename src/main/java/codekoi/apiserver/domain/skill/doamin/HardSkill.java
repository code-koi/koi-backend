package codekoi.apiserver.domain.skill.doamin;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@DiscriminatorValue("hardSkill")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder

public class HardSkill extends Skill {

    private Integer searchCount;


}
