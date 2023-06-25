package codekoi.apiserver.domain.code.comment.repository;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CodeReviewCommentRepository extends JpaRepository<CodeReviewComment, Long> {

    int countByUserId(Long userId);

    @Query("select cr from CodeReviewComment cr where cr.user.id = :id")
    List<CodeReviewComment> findByUser(@Param("id") Long userId);

    @Query("select cr from CodeReviewComment cr where cr.codeReview.id = :id")
    List<CodeReviewComment> findByCodeReviewId(@Param("id") Long codeReviewId);
}
