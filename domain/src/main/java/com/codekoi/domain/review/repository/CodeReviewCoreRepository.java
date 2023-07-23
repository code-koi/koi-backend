package com.codekoi.domain.review.repository;

import com.codekoi.domain.review.entity.CodeReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeReviewCoreRepository extends JpaRepository<CodeReview, Long> {

    default CodeReview getOneById(Long codeReviewId) {
        return this.findById(codeReviewId).orElseThrow(() -> {
            throw new CodeReviewNotFoundException(codeReviewId);
        });
    }
}
