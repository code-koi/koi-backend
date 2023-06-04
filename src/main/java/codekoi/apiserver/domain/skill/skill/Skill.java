package codekoi.apiserver.domain.skill.skill;

import codekoi.apiserver.domain.model.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "skill_type")
public abstract class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long id;

    private String name;
}
