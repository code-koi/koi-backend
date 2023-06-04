package codekoi.apiserver.domain.mentor.schedule.domain;

import codekoi.apiserver.domain.mentor.domain.Mentor;
import codekoi.apiserver.domain.model.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MentorSchedule extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentor_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    private String date;
    private String startTime;
    private String endTile;
}
