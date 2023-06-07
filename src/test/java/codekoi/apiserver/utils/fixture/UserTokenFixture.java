package codekoi.apiserver.utils.fixture;

import codekoi.apiserver.domain.auth.domain.UserToken;
import codekoi.apiserver.domain.user.domain.User;

import java.time.LocalDateTime;

public enum UserTokenFixture {
    EXPIRED_TOKEN("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwiaWF0IjoxNjg2MTE5MjYzLCJleHAiOjE2ODY3MjQwNjN9.3elRxRRR4Moa6U5TLHd2lC0yvN6TLiLu7on37Kadb2o",
            LocalDateTime.now().minusDays(1)),
    VALID_TOKEN("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwiaWF0IjoxNjg2MTE5MjYzLCJleHAiOjE2ODY3MjQwNjN9.3elRxRRR4Moa6U5TLHd2lC0yvN6TLiLu7on37Kadb2o",
            LocalDateTime.now().plusDays(10)),
    ;

    public final String refreshToken;
    public final LocalDateTime expiredAt;

    UserTokenFixture(String refreshToken, LocalDateTime expiredAt) {
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
    }

    public UserToken toUserToken(User user) {
        return UserToken.builder()
                .user(user)
                .expiredAt(expiredAt)
                .refreshToken(refreshToken)
                .build();
    }
}
