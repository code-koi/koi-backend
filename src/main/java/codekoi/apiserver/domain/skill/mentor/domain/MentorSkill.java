package codekoi.apiserver.domain.skill.mentor.domain;

import codekoi.apiserver.domain.mentor.domain.Mentor;
import codekoi.apiserver.domain.model.TimeBaseEntity;
import codekoi.apiserver.domain.skill.skill.SoftSkill;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MentorSkill extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentor_skill_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "skill_id")
    private SoftSkill skill;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;
}
