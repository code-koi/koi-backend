package com.codekoi.domain.skill.user.entity;

import com.codekoi.domain.skill.skill.entity.Skill;
import com.codekoi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "user_skill")
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_skill_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    protected UserSkill() {
    }

    @Builder
    private UserSkill(Long id, Skill skill, User user) {
        this.id = id;
        this.skill = skill;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Skill getSkill() {
        return skill;
    }

    public User getUser() {
        return user;
    }
}
