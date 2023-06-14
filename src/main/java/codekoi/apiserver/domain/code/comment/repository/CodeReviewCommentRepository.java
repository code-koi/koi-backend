package codekoi.apiserver.domain.code.comment.repository;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeReviewCommentRepository extends JpaRepository<CodeReviewComment, Long> {

    int countByUserId(Long userId);
}
