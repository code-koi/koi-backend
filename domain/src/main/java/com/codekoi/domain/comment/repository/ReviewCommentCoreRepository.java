package com.codekoi.domain.comment.repository;

import com.codekoi.domain.comment.entity.ReviewComment;
import com.codekoi.domain.comment.exception.CommentNotFoundException;
import com.codekoi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewCommentCoreRepository extends JpaRepository<ReviewComment, Long> {

    int countByUserId(Long userId);

    List<ReviewComment> findTop10ByUserOrderByCreatedAtDesc(User user);

    @Query("select rc from ReviewComment rc where rc.user.id = :id")
    List<ReviewComment> findByUserId(@Param("id") Long userId);

    @Query("select rc from ReviewComment rc where rc.codeReview.id = :id")
    List<ReviewComment> findByCodeReviewId(@Param("id") Long codeReviewId);

    default ReviewComment getOneById(Long commentId) {
        return this.findById(commentId).orElseThrow(() -> {
            throw new CommentNotFoundException(commentId);
        });
    }
}
