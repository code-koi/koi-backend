package com.codekoi.apiserver.favorite.repository;

import com.codekoi.domain.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {

    @EntityGraph(attributePaths = {"codeReview"})
    List<Favorite> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

    @EntityGraph(attributePaths = {"codeReview", "codeReview.user"})
    List<Favorite> findAllByUserId(Long userId);

    Optional<Favorite> findByUserId(Long userId);

    List<Favorite> findByUserIdAndCodeReviewIdIn(Long userId, List<Long> codeReviewIds);
}
