package codekoi.apiserver.domain.code.like.repository;

import codekoi.apiserver.domain.code.like.domain.Like;
import codekoi.apiserver.domain.code.like.exception.LikeNotFoundException;
import codekoi.apiserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {

    @EntityGraph(attributePaths = {"comment"})
    List<Like> findTop10ByUserOrderByCreatedAtDesc(User user);

    @Query("select l from Like l where l.user.id = :userId and l.comment.id = :commentId")
    Optional<Like> findByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);

    @Query("select l from Like l where l.comment.id in :commentIds")
    List<Like> findByCommentIdIn(@Param("commentIds") List<Long> commentIds);

    default Like findByLikeId(Long likeId) {
        return this.findById(likeId).orElseThrow(LikeNotFoundException::new);
    }

}
