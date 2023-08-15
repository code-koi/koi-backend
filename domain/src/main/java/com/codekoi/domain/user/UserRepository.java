package com.codekoi.domain.user;

import com.codekoi.domain.user.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    default User getOneById(Long userId) {
        return this.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException(userId);
        });
    }
}
