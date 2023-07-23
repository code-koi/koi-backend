package com.codekoi.domain.skill.review.entity;

import com.codekoi.domain.review.entity.CodeReview;
import com.codekoi.domain.skill.skill.entity.Skill;
import jakarta.persistence.*;
import lombok.Builder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "code_review_skill")
public class CodeReviewSkill {

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

    protected CodeReviewSkill() {
    }

    @Builder
    private CodeReviewSkill(Long id, Skill skill, CodeReview codeReview) {
        this.id = id;
        this.skill = skill;
        this.codeReview = codeReview;
    }

    public Long getId() {
        return id;
    }

    public Skill getSkill() {
        return skill;
    }

    public CodeReview getCodeReview() {
        return codeReview;
    }
}
