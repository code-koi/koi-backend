package codekoi.apiserver.domain.user.repository;

import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.exception.UserNotFoundException;
import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailValue(String email);

    default User findUserById(Long userId) {
        final User user = this.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException();
        });
        if (user.getCanceledAt() != null) {
            throw new UserNotFoundException();
        }
        return user;
    }
}
