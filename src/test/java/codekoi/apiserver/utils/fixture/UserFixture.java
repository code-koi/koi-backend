package codekoi.apiserver.utils.fixture;

import codekoi.apiserver.domain.user.domain.User;

public enum UserFixture {
    SUNDO("만나서 반갑습니다!", "김선도", "sdcodebase@gmail.com", 2, "test.com/sundo.png"),
    ;

    public final String introduce;
    public final String nickname;
    public final String email;
    public final int years;
    public final String profileImageUrl;

    UserFixture(String introduce, String nickname, String email, int years, String profileImageUrl) {
        this.introduce = introduce;
        this.nickname = nickname;
        this.email = email;
        this.years = years;
        this.profileImageUrl = profileImageUrl;
    }

    public User toUser() {
        return User.builder()
                .introduce(introduce)
                .nickname(nickname)
                .email(email)
                .years(years)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
