package codekoi.apiserver.domain.code.review.repository;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.Favorite;
import codekoi.apiserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeFavoriteRepository extends JpaRepository<Favorite, Long> {

    @EntityGraph(attributePaths = {"codeReview", "codeReview.user"})
    List<Favorite> findFavoriteByUser(User user);

    List<Favorite> findFavoriteByUserAndCodeReviewIn(User user, List<CodeReview> codeReviews);
}
