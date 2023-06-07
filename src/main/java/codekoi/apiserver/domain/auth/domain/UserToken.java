package codekoi.apiserver.domain.auth.domain;

import codekoi.apiserver.domain.model.TimeBaseEntity;
import codekoi.apiserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserToken extends TimeBaseEntity {

    public static final int REFRESH_TOKEN_VALID_DURATION = 604800000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_token_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime expiredAt;

    public String refreshToken;

    @Builder
    private UserToken(Long id, User user, LocalDateTime expiredAt, String refreshToken) {
        this.id = id;
        this.user = user;
        this.expiredAt = expiredAt;
        this.refreshToken = refreshToken;
    }

    private UserToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.expiredAt = LocalDateTime.now().plusSeconds(REFRESH_TOKEN_VALID_DURATION / 1000);
    }

    public static UserToken of(User user, String refreshToken) {
        return new UserToken(user, refreshToken);
    }

    public boolean isUserMatched(Long userId) {
        return user.getId().equals(userId);
    }
}
