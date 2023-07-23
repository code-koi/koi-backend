package com.codekoi.domain.review.repository;

import com.codekoi.domain.review.entity.CodeReview;
import com.codekoi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeReviewCoreRepository extends JpaRepository<CodeReview, Long> {

    List<CodeReview> findByUser(User user);

    List<CodeReview> findTop10ByUserOrderByCreatedAtDesc(User user);

    default CodeReview getOneById(Long codeReviewId) {
        return this.findById(codeReviewId).orElseThrow(() -> {
            throw new CodeReviewNotFoundException(codeReviewId);
        });
    }
}
