package com.codekoi.domain.like;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    @EntityGraph(attributePaths = {"comment"})
    List<Like> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);

    List<Like> findByUserIdAndCommentIdIn(Long userId, List<Long> commentIds);

    List<Like> findByCommentIdIn(List<Long> commentIds);

}
