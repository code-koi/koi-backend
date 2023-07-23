package com.codekoi.domain.favorite.repository;

import com.codekoi.domain.favorite.entity.Favorite;
import com.codekoi.domain.review.entity.CodeReview;
import com.codekoi.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//todo: queryDSL 모듈 추가
@Repository
public interface FavoriteCoreRepository extends JpaRepository<Favorite, Long> {

    @EntityGraph(attributePaths = {"codeReview"})
    List<Favorite> findTop10ByUserOrderByCreatedAtDesc(User user);

    @EntityGraph(attributePaths = {"codeReview", "codeReview.user"})
    List<Favorite> findFavoritesByUser(User user);

    Optional<Favorite> findByUser(User user);

    List<Favorite> findFavoriteByUserAndCodeReviewIn(User user, List<CodeReview> codeReviews);
}
