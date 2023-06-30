package codekoi.apiserver.domain.code.like.repository;

import codekoi.apiserver.domain.code.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

}
