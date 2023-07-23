package com.codekoi.domain.like.repository;

import com.codekoi.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeCoreRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);

}
