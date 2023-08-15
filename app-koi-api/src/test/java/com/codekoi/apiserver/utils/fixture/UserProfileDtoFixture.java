package com.codekoi.apiserver.utils.fixture;

import com.codekoi.apiserver.user.dto.UserProfileDto;

public enum UserProfileDtoFixture {
    PROFILE1("code-koi.com/image/sundo.png", "sdcodebase", 1L);

    public String profileImage;
    public String nickname;
    public Long id;

    UserProfileDtoFixture(String profileImage, String nickname, Long id) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.id = id;
    }

    public UserProfileDto toUserProfileDto() {
        return new UserProfileDto(PROFILE1.profileImage, PROFILE1.nickname, PROFILE1.id);
    }
}
