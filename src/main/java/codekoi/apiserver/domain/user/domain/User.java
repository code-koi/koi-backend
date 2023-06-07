package codekoi.apiserver.domain.user.domain;

import codekoi.apiserver.domain.model.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
}
