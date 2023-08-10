package com.codekoi.fixture;


import com.codekoi.domain.authtoken.AuthToken;
import com.codekoi.domain.user.User;

import java.time.LocalDateTime;

public enum AuthTokenFixture {
    EXPIRED_TOKEN("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwiaWF0IjoxNjg2MTE5MjYzLCJleHAiOjE2ODY3MjQwNjN9.3elRxRRR4Moa6U5TLHd2lC0yvN6TLiLu7on37Kadb2o",
            LocalDateTime.now().minusDays(1)),
    VALID_TOKEN("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwiaWF0IjoxNjg2MTE5MjYzLCJleHAiOjE2ODY3MjQwNjN9.3elRxRRR4Moa6U5TLHd2lC0yvN6TLiLu7on37Kadb2o",
            LocalDateTime.now().plusDays(10)),
    ;

    public final String refreshToken;
    public final LocalDateTime expiredAt;

    AuthTokenFixture(String refreshToken, LocalDateTime expiredAt) {
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
    }

    public AuthToken toAuthToken(User user) {
        return AuthToken.builder()
                .user(user)
                .expiredAt(expiredAt)
                .refreshToken(refreshToken)
                .build();
    }
}
