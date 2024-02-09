package com.codekoi.review;

import com.codekoi.skill.Skill;
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

    @Override
    public String toString() {
        return "CodeReviewSkill{" +
                "id=" + id +
                ", skill=" + skill.getId() +
                ", codeReview=" + codeReview.getId() +
                '}';
    }
}
