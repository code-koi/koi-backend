package codekoi.apiserver.domain.code.review.dto;

import codekoi.apiserver.domain.code.review.vo.Activity;
import codekoi.apiserver.domain.code.review.vo.ActivityHistories;
import codekoi.apiserver.global.util.time.BeforeTimeSerializer;
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
