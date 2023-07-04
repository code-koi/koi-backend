package codekoi.apiserver.domain.code.review.repository;

import codekoi.apiserver.domain.code.review.domain.CodeReviewSkill;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CodeReviewSkillRepository extends JpaRepository<CodeReviewSkill, Long> {

    @EntityGraph(attributePaths = {"skill"})
    @Query("select rs from CodeReviewSkill rs where rs.codeReview.id in :ids")
    List<CodeReviewSkill> findByCodeReviewIdsIn(@Param("ids") List<Long> ids);

}
