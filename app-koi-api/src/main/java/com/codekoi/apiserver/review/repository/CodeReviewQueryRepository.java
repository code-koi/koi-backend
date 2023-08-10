package com.codekoi.apiserver.review.repository;

import com.codekoi.domain.review.CodeReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeReviewQueryRepository extends JpaRepository<CodeReview, Long> {


    List<CodeReview> findByUserId(Long userId);

    List<CodeReview> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

}
