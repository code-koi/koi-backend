package com.codekoi.domain.comment;

import com.codekoi.domain.comment.exception.CommentNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    default ReviewComment getOneById(Long commentId) {
        return this.findById(commentId).orElseThrow(() -> {
            throw new CommentNotFoundException(commentId);
        });
    }
}
