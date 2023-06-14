package codekoi.apiserver.domain.code.review.domain;

import codekoi.apiserver.domain.model.TimeBaseEntity;
import codekoi.apiserver.domain.skill.doamin.HardSkill;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "skill_id")
    private HardSkill skill;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "code_review_id")
    private CodeReview codeReview;
}
