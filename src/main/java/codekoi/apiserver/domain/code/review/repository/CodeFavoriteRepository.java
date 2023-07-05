package codekoi.apiserver.domain.code.review.repository;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.Favorite;
import codekoi.apiserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeFavoriteRepository extends JpaRepository<Favorite, Long> {

    @EntityGraph(attributePaths = {"codeReview"})
    List<Favorite> findTop10ByUserOrderByCreatedAtDesc(User user);

    @EntityGraph(attributePaths = {"codeReview", "codeReview.user"})
    List<Favorite> findFavoritesByUser(User user);

    Optional<Favorite> findByUser(User user);

    List<Favorite> findFavoriteByUserAndCodeReviewIn(User user, List<CodeReview> codeReviews);
}
