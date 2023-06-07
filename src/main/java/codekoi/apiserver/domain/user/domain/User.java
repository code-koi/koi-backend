package codekoi.apiserver.domain.user.domain;

import codekoi.apiserver.domain.model.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Where(clause = "canceled_at is null")
public class User extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    @Embedded
    private Introduce introduce;

    @Embedded
    private Nickname nickname;

    @Embedded
    private Email email;

    @Embedded
    private Years years;

    private String profileImageUrl;

    @Builder
    private User(Long id, String introduce, String nickname, String email, int years, String profileImageUrl) {
        this.id = id;
        this.introduce = new Introduce(introduce);
        this.nickname = new Nickname(nickname);
        this.email = new Email(email);
        this.years = new Years(years);
        this.profileImageUrl = profileImageUrl;
    }

    public String getIntroduce() {
        return introduce.getValue();
    }

    public String getNickname() {
        return nickname.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public int getYears() {
        return years.getValue();
    }
}
