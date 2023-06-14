package codekoi.apiserver.domain.code.review.repository;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeReviewRepository extends JpaRepository<CodeReview, Long> {


}
