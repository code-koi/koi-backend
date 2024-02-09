package com.codekoi.user;

import com.codekoi.skill.Skill;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Where(clause = "canceled_at is null")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    private String introduce;

    private String nickname;

    private String email;
    private int years;
    private String profileImageUrl;

    //todo: 양방향 의존관계 제거하기
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSkill> skills = new ArrayList<>();

    protected User() {
    }

    @Builder
    private User(Long id, String introduce, String nickname, String email, int years, String profileImageUrl) {
        this.id = id;
        this.introduce = introduce;
        this.nickname = nickname;
        this.email = email;
        this.years = years;
        this.profileImageUrl = profileImageUrl;
    }

    public void addUserSkill(Skill skill) {
        final UserSkill userSkill = UserSkill.builder()
                .skill(skill)
                .user(this)
                .build();
        this.skills.add(userSkill);
    }

    public Long getId() {
        return id;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public int getYears() {
        return years;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public List<UserSkill> getSkills() {
        return skills;
    }

    public List<Long> getSkillIds() {
        return skills.stream()
                .map(UserSkill::getId)
                .collect(Collectors.toList());
    }
}
