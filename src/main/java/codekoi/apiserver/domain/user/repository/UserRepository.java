package codekoi.apiserver.domain.user.repository;

import codekoi.apiserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
