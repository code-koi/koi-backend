package com.codekoi.domain.authtoken.repository;

import com.codekoi.domain.authtoken.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenCoreRepository extends JpaRepository<AuthToken, Long> {

    Optional<AuthToken> findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);
}
