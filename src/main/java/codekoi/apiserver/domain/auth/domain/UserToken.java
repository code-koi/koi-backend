package codekoi.apiserver.domain.auth.domain;

import codekoi.apiserver.domain.model.TimeBaseEntity;
import codekoi.apiserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserToken extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_token_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime expiredAt;

    public String refreshToken;

    public boolean isUserMatched(Long userId) {
        return user.getId().equals(userId);
    }
}
