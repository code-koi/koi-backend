package com.codekoi.domain.user.repository;

import com.codekoi.domain.user.entity.User;
import com.codekoi.domain.user.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCoreRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    default User getOneById(Long userId) {
        return this.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException(userId);
        });
    }
}
