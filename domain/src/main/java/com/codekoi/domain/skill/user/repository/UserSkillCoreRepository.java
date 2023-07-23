package com.codekoi.domain.skill.user.repository;

import com.codekoi.domain.skill.user.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSkillCoreRepository extends JpaRepository<UserSkill, Long> {

}
