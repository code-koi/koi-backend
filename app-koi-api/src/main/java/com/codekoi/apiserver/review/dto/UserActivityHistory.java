package com.codekoi.apiserver.review.dto;

import com.codekoi.apiserver.review.vo.Activity;
import com.codekoi.coreweb.formatter.BeforeTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
public class UserActivityHistory {
    private Long reviewId;
    private String log;

    @JsonSerialize(using = BeforeTimeSerializer.class)
    private LocalDateTime createdAt;

    public UserActivityHistory(Long reviewId, String log, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.log = log;
        this.createdAt = createdAt;
    }

    public static List<UserActivityHistory> listFrom(List<Activity> activities) {
        return activities.stream()
                .map(UserActivityHistory::from)
                .toList();
    }

    public static UserActivityHistory from(Activity a) {
        return new UserActivityHistory(a.id(), mapLog(a.targetText(), a.type()), a.createdAt());
    }

    private static String mapLog(String content, Activity.Type type) {
        final String logPrefix = "\"" + content + "\" ";
        return logPrefix + type.description;
    }
}
