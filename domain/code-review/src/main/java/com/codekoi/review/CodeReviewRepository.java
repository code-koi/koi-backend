package com.codekoi.review;

import com.codekoi.review.exception.CodeReviewNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeReviewRepository extends JpaRepository<CodeReview, Long> {

    List<CodeReview> findByUserId(Long userId);

    List<CodeReview> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    default CodeReview getOneById(Long codeReviewId) {
        return this.findById(codeReviewId).orElseThrow(() -> {
            throw new CodeReviewNotFoundException(codeReviewId);
        });
    }
}
