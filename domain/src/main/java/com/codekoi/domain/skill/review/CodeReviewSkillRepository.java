package com.codekoi.domain.skill.review;

import com.codekoi.domain.skill.review.CodeReviewSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeReviewSkillRepository extends JpaRepository<CodeReviewSkill, Long> {

}
