package codekoi.apiserver.domain.code.review.domain;

import codekoi.apiserver.domain.model.TimeBaseEntity;
import codekoi.apiserver.domain.skill.domain.Skill;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CodeReviewSkill extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_review_skill")
    private Long id;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "code_review_id")
    private CodeReview codeReview;

    @Builder
    private CodeReviewSkill(Long id, Skill skill, CodeReview codeReview) {
        this.id = id;
        this.skill = skill;
        this.codeReview = codeReview;
    }
}
