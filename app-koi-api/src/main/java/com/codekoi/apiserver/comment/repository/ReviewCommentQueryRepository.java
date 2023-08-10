package com.codekoi.apiserver.comment.repository;

import com.codekoi.domain.comment.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewCommentQueryRepository extends JpaRepository<ReviewComment, Long> {

    int countByUserId(Long userId);

    List<ReviewComment> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    List<ReviewComment> findByUserId(Long userId);

    List<ReviewComment> findByCodeReviewId(Long codeReviewId);
}
