package com.codekoi.domain.skill.review.repository;

import com.codekoi.domain.skill.review.entity.CodeReviewSkill;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeReviewSkillCoreRepository extends JpaRepository<CodeReviewSkill, Long> {

    @EntityGraph(attributePaths = {"skill"})
    @Query("select rs from CodeReviewSkill rs where rs.codeReview.id in :ids")
    List<CodeReviewSkill> findByCodeReviewIdsIn(@Param("ids") List<Long> ids);
}
