package codekoi.apiserver.domain.user.domain;

import codekoi.apiserver.domain.model.TimeBaseEntity;
import codekoi.apiserver.domain.skill.doamin.Skill;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserSkill extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_skill_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill hardSkill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @Builder
    private UserSkill(Long id, Skill hardSkill, User user) {
        this.id = id;
        this.hardSkill = hardSkill;
        this.user = user;
    }
}
