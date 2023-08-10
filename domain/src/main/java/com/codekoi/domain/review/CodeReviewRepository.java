package com.codekoi.domain.review;

import com.codekoi.domain.review.exception.CodeReviewNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeReviewRepository extends JpaRepository<CodeReview, Long> {

    default CodeReview getOneById(Long codeReviewId) {
        return this.findById(codeReviewId).orElseThrow(() -> {
            throw new CodeReviewNotFoundException(codeReviewId);
        });
    }
}
