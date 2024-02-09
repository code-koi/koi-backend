package com.codekoi.review;

import com.codekoi.review.exception.CommentNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long>, ReviewCommentRepositoryCustom {

    int countByUserId(Long userId);

    List<ReviewComment> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    List<ReviewComment> findByUserId(Long userId);

    List<ReviewComment> findByCodeReviewId(Long codeReviewId);


    default ReviewComment getOneById(Long commentId) {
        return this.findById(commentId).orElseThrow(() -> {
            throw new CommentNotFoundException(commentId);
        });
    }
}
