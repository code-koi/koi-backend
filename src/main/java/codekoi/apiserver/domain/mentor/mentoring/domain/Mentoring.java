package codekoi.apiserver.domain.mentor.mentoring.domain;

import codekoi.apiserver.domain.mentor.domain.Mentor;
import codekoi.apiserver.domain.mentor.schedule.domain.MentorSchedule;
import codekoi.apiserver.domain.model.TimeBaseEntity;
import codekoi.apiserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Mentoring extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentoring_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_schdule_id")
    private MentorSchedule mentorSchedule;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee_user_id")
    private User mentee;
}
