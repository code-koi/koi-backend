package codekoi.apiserver.domain.code.review.domain;

import codekoi.apiserver.domain.model.TimeBaseEntity;
import codekoi.apiserver.domain.skill.doamin.HardSkill;
import codekoi.apiserver.domain.skill.doamin.Skill;
import codekoi.apiserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    @Builder
    private CodeReview(Long id, User user, String title, String content, CodeReviewStatus status) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public static CodeReview of(User user, String title, String content) {
        return CodeReview.builder()
                .user(user)
                .title(title)
                .content(content)
                .status(CodeReviewStatus.PENDING)
                .build();
    }

    public void addCodeReviewSkill(HardSkill skill) {
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
}
