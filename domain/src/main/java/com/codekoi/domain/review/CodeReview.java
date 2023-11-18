package com.codekoi.domain.review;

import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.skill.review.CodeReviewSkill;
import com.codekoi.domain.skill.skill.Skill;
import com.codekoi.domain.user.User;
import com.codekoi.model.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "code_review")
public class CodeReview extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_reviewee_users_id")
    private User user;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private CodeReviewStatus status;

    @OneToMany(mappedBy = "codeReview", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<CodeReviewSkill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "codeReview", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<ReviewComment> comments = new ArrayList<>();

    protected CodeReview() {
    }

    @Builder
    private CodeReview(Long id, User user, String title, String content, CodeReviewStatus status, List<CodeReviewSkill> skills, List<ReviewComment> comments) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.status = status;
        this.skills = Objects.requireNonNullElse(skills, new ArrayList<>());
        this.comments = Objects.requireNonNullElse(comments, new ArrayList<>());
    }

    public static CodeReview of(User user, String title, String content) {
        return CodeReview.builder()
                .user(user)
                .title(title)
                .content(content)
                .status(CodeReviewStatus.PENDING)
                .build();
    }

    public void addCodeReviewSkill(Skill skill) {
        final CodeReviewSkill reviewSkill = CodeReviewSkill.builder()
                .skill(skill)
                .codeReview(this)
                .build();

        this.skills.add(reviewSkill);
    }

    public List<String> getSkillNames() {
        return skills.stream()
                .map(CodeReviewSkill::getSkill)
                .map(Skill::getName)
                .collect(Collectors.toList());
    }

    public boolean isMyCodeReview(Long userId) {
        return user.getId().equals(userId);
    }

    public void update(String title, String content, List<Skill> skills) {
        this.title = title;
        this.content = content;

        this.skills.clear();
        skills.forEach(this::addCodeReviewSkill);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public CodeReviewStatus getStatus() {
        return status;
    }

    public List<CodeReviewSkill> getSkills() {
        return skills;
    }

    public List<ReviewComment> getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "CodeReview{" +
                "id=" + id +
                ", userId=" + user.getId() +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                '}';
    }
}
