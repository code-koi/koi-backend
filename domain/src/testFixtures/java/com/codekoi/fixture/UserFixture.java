package com.codekoi.fixture;


import com.codekoi.domain.user.User;

public enum UserFixture {
    SUNDO("만나서 반갑습니다!", "김선도", "sdcodebase@gmail.com", 2, "test.com/sundo.png"),
    HONG("안녕하세요 반가워요", "홍길동", "hong@gmail.com", 5, "test.com/hong.png"),
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

    public User toUser(Long userId) {
        return getUser(userId);
    }

    public User toUser() {
        return getUser(null);
    }

    private User getUser(Long userId) {
        return User.builder()
                .id(userId)
                .introduce(introduce)
                .nickname(nickname)
                .email(email)
                .years(years)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
