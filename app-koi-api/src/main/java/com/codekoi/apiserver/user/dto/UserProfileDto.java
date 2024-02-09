package com.codekoi.apiserver.user.dto;


import com.codekoi.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Objects;

//todo: 도메인 모듈로 이동하기
@Getter
public class UserProfileDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profileImageUrl;

    private String nickname;

    private Long id;

    public UserProfileDto(String profileImageUrl, String nickname, Long id) {
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.id = id;
    }

    public static UserProfileDto from(User user) {
        return new UserProfileDto(user.getProfileImageUrl(), user.getNickname(), user.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UserProfileDto that = (UserProfileDto) o;

        if (!Objects.equals(profileImageUrl, that.profileImageUrl))
            return false;
        if (!nickname.equals(that.nickname)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        int result = profileImageUrl != null ? profileImageUrl.hashCode() : 0;
        result = 31 * result + nickname.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }
}