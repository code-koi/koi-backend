package com.codekoi.apiserver.review.dto;

import com.codekoi.apiserver.user.dto.UserProfileDto;
import com.codekoi.coreweb.formatter.BeforeTimeSerializer;
import com.codekoi.favorite.Favorite;
import com.codekoi.review.CodeReview;
import com.codekoi.review.CodeReviewStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserCodeReviewDto {

    private UserProfileDto user;

    @JsonSerialize(using = BeforeTimeSerializer.class)
    private LocalDateTime createdAt;

    private String title;

    private List<String> skills = new ArrayList<>();

    private CodeReviewStatus status;

    private Long reviewId;

    private Boolean isFavorite;

    public UserCodeReviewDto(UserProfileDto user, LocalDateTime createdAt, String title, List<String> skills, CodeReviewStatus status, Long reviewId, boolean isFavorite) {
        this.user = user;
        this.createdAt = createdAt;
        this.title = title;
        this.skills = skills;
        this.status = status;
        this.reviewId = reviewId;
        this.isFavorite = isFavorite;
    }

    public static List<UserCodeReviewDto> listOf(List<CodeReview> reviews, List<Favorite> favorites, boolean me) {
        final Map<Long, Favorite> favoriteMap = favorites.stream()
                .collect(Collectors.toMap(f -> f.getCodeReview().getId(), favorite -> favorite));

        return reviews.stream()
                .map(r -> of(isFavorite(me, favoriteMap, r), r))
                .toList();
    }

    public static List<UserCodeReviewDto> listOf(List<Favorite> favorites, boolean me) {
        return favorites.stream()
                .map(Favorite::getCodeReview)
                .map(r -> of(me, r))
                .toList();
    }

    public static UserCodeReviewDto of(boolean me, CodeReview r) {
        return new UserCodeReviewDto(
                UserProfileDto.from(r.getUser()),
                r.getCreatedAt(),
                r.getTitle(),
                r.getSkillNames(),
                r.getStatus(),
                r.getId(),
                me
        );
    }

    private static boolean isFavorite(boolean me, Map<Long, Favorite> favoriteMap, CodeReview r) {
        return me && favoriteMap.containsKey(r.getId());
    }
}
