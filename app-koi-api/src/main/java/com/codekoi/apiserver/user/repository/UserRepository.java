package com.codekoi.apiserver.user.repository;

import com.codekoi.domain.user.entity.User;
import com.codekoi.domain.user.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    default User getOneById(Long userId) {
        return this.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException(userId);
        });
    }
}
