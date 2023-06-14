package codekoi.apiserver.domain.user.dto;

import codekoi.apiserver.domain.user.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class UserDetail {
    private boolean me;
    private String nickname;
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profileImageUrl;
    private int years;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String introduce;

    private Activity activity;
    private List<Skill> skills = new ArrayList<>();

    public UserDetail(boolean me, String nickname, String email, String profileImageUrl, int years, String introduce, Activity activity, List<Skill> skills) {
        this.me = me;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.years = years;
        this.introduce = introduce;
        this.activity = activity;
        this.skills = skills;
    }

    public static UserDetail of(User user, int reviewCount, Long sessionUserId) {
        final List<Skill> skills = user.getSkills()
                .stream()
                .map(s -> new Skill(s.getHardSkill().getId(), s.getHardSkill().getName()))
                .toList();

        final boolean me = user.getId().equals(sessionUserId);
        return new UserDetail(me, user.getNickname(), user.getEmail(), user.getProfileImageUrl(), user.getYears(),
                user.getIntroduce(), new Activity(reviewCount), skills);
    }

    @Getter
    public static class Activity {
        //        private String mentoring;
        private int codeReview;

        public Activity(int codeReview) {
            this.codeReview = codeReview;
        }
    }

    @Getter
    public static class Skill {
        private Long id;
        private String name;

        public Skill(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

}
