package com.codekoi.domain.skill.skill.entity;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "skill")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long id;

    private String name;

    private Integer searchCount;

    protected Skill() {
    }

    @Builder
    private Skill(Long id, String name, Integer searchCount) {
        this.id = id;
        this.name = name;
        this.searchCount = searchCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Skill that = (Skill) o;

        if (!id.equals(that.id)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getSearchCount() {
        return searchCount;
    }
}
