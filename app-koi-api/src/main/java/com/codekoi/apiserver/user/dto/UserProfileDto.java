package com.codekoi.apiserver.user.dto;


import com.codekoi.domain.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

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
}