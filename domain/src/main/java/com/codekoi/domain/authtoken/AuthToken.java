package com.codekoi.domain.authtoken;

import com.codekoi.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "auth_token")
public class AuthToken {

    public static final int REFRESH_TOKEN_VALID_DURATION = 604800000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_token_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime expiredAt;

    public String refreshToken;

    protected AuthToken() {
    }

    @Builder
    private AuthToken(Long id, User user, LocalDateTime expiredAt, String refreshToken) {
        this.id = id;
        this.user = user;
        this.expiredAt = expiredAt;
        this.refreshToken = refreshToken;
    }

    private AuthToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.expiredAt = LocalDateTime.now().plusSeconds(REFRESH_TOKEN_VALID_DURATION / 1000);
    }

    public static AuthToken of(User user, String refreshToken) {
        return new AuthToken(user, refreshToken);
    }

    public boolean isUserMatched(Long userId) {
        return user.getId().equals(userId);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
