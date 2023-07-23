package com.codekoi.domain.skill.skill.repository;

import com.codekoi.domain.skill.skill.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillCoreRepository extends JpaRepository<Skill, Long> {

}