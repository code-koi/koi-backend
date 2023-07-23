package com.codekoi.apiserver.review.dto;

import com.codekoi.apiserver.review.vo.Activity;
import com.codekoi.apiserver.review.vo.ActivityHistories;
import com.codekoi.time.annotation.BeforeTimeJsonSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
public class UserActivityHistory {
    private Long reviewId;
    private String log;

    @BeforeTimeJsonSerializer
    private LocalDateTime createdAt;

    public UserActivityHistory(Long reviewId, String log, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.log = log;
        this.createdAt = createdAt;
    }

    public static List<UserActivityHistory> listFrom(List<Activity> activities) {
        return activities.stream()
                .map(a -> new UserActivityHistory(a.id(), mapLog(a.targetText(), a.type()), a.createdAt()))
                .toList();
    }

    private static String mapLog(String content, ActivityHistories.Type type) {
        final String c = "\"" + content + "\"";
        switch (type) {
            case REVIEW -> {
                return c + " 리뷰 요청";
            }
            case LIKE -> {
                return c + " 좋아요";
            }
            case COMMENT -> {
                return c + " 리뷰 작성";
            }
            case FAVORITE -> {
                return c + " 즐겨찾기";
            }
        }
        throw new IllegalArgumentException();
    }
}
