package com.codekoi.domain.skill.user;

import com.codekoi.domain.skill.user.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

}
