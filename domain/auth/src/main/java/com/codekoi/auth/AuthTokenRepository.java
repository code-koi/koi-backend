package com.codekoi.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    Optional<AuthToken> findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);
}
