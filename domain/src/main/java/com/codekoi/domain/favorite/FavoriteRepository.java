package com.codekoi.domain.favorite;

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

    Optional<Favorite> findByUserIdAndCodeReviewId(Long userId, Long codeReviewId);

    List<Favorite> findByUserIdAndCodeReviewIdIn(Long userId, List<Long> codeReviewIds);


}
