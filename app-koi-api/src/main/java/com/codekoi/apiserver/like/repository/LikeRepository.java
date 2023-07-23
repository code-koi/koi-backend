package com.codekoi.apiserver.like.repository;

import com.codekoi.domain.like.entity.Like;
import com.codekoi.domain.like.exception.LikeNotFoundException;
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

    List<Like> findByCommentIdIn(List<Long> commentIds);

    default Like getOneById(Long likeId) {
        return this.findById(likeId).orElseThrow(LikeNotFoundException::new);
    }
}
