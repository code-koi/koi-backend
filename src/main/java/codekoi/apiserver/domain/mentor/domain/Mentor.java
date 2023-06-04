package codekoi.apiserver.domain.mentor.domain;

import codekoi.apiserver.domain.model.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Mentor extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentor_id")
    private Long id;

    private String title;
    private String introduce;

    private Double averageGrade;
    private Integer reviewCount;
}
