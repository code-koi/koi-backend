package com.codekoi.apiserver.skill.review.repository;

import com.codekoi.domain.skill.review.entity.CodeReviewSkill;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeReviewSkillRepository extends JpaRepository<CodeReviewSkill, Long> {

    @EntityGraph(attributePaths = {"skill"})
    List<CodeReviewSkill> findByCodeReviewIdIn(List<Long> codeReviewIds);

}
