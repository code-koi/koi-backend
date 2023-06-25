package codekoi.apiserver.domain.code.review.repository;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.exception.CodeReviewNotFoundException;
import codekoi.apiserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeReviewRepository extends JpaRepository<CodeReview, Long>, CodeReviewRepositoryCustom {


    List<CodeReview> findByUser(User user);

    @EntityGraph(attributePaths = {"user"})
    @Query("select c from CodeReview c where c.id=:id")
    Optional<CodeReview> findByCodeReviewIdWithUser(@Param("id") Long codeReviewId);

    default CodeReview findByCodeReviewId(Long codeReviewId) {
        final CodeReview codeReview = this.findById(codeReviewId)
                .orElseThrow(CodeReviewNotFoundException::new);

        if (codeReview.getCanceledAt() != null) {
            throw new CodeReviewNotFoundException();
        }
        return codeReview;
    }
}
