package com.codekoi.review;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    @EntityGraph(attributePaths = {"comment"})
    List<CommentLike> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId);

    List<CommentLike> findByUserIdAndCommentIdIn(Long userId, List<Long> commentIds);

    List<CommentLike> findByCommentIdIn(List<Long> commentIds);

}
