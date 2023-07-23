package com.codekoi.domain.skill.review.repository;

import com.codekoi.domain.skill.review.entity.CodeReviewSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeReviewSkillCoreRepository extends JpaRepository<CodeReviewSkill, Long> {

}
