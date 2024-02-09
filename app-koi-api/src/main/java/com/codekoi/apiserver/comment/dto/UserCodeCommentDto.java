package com.codekoi.apiserver.comment.dto;

import com.codekoi.apiserver.user.dto.UserProfileDto;
import com.codekoi.coreweb.formatter.BeforeTimeSerializer;
import com.codekoi.koi.KoiHistory;
import com.codekoi.koi.KoiType;
import com.codekoi.review.CommentLike;
import com.codekoi.review.ReviewComment;
import com.codekoi.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UserCodeCommentDto {

    private UserProfileDto user;

    private Long reviewId;

    @JsonSerialize(using = BeforeTimeSerializer.class)
    private LocalDateTime createdAt;

    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private KoiType koiType;

    private long likeCount;

    public UserCodeCommentDto(UserProfileDto user, Long reviewId, LocalDateTime createdAt, String content, KoiType koiType, Long likeCount) {
        this.user = user;
        this.reviewId = reviewId;
        this.createdAt = createdAt;
        this.content = content;
        this.koiType = koiType;
        this.likeCount = likeCount;
    }

    public static UserCodeCommentDto of(User user, ReviewComment comment, KoiType koiType, Long likeCount) {
        final UserProfileDto userDto = UserProfileDto.from(user);
        return new UserCodeCommentDto(userDto, comment.getCodeReview().getId(), comment.getCreatedAt(), comment.getContent(),
                koiType, likeCount);
    }

    public static List<UserCodeCommentDto> listOf(User user, List<ReviewComment> comments, List<KoiHistory> koiHistories, List<CommentLike> likes) {
        final Map<Long, KoiType> koiMap = getKoiMap(koiHistories);
        final Map<Long, Long> likeCountMap = getLikeCountMap(likes);

        return comments.stream()
                .map(c -> UserCodeCommentDto.of(user, c, koiMap.get(c.getId()), likeCountMap.getOrDefault(c.getId(), 0L)))
                .toList();
    }

    private static Map<Long, KoiType> getKoiMap(List<KoiHistory> koiHistories) {
        return koiHistories.stream()
                .collect(Collectors.toMap(koiHistory -> koiHistory.getCodeReviewComment().getId(),
                        KoiHistory::getKoiType));
    }

    private static Map<Long, Long> getLikeCountMap(List<CommentLike> likes) {
        return likes.stream()
                .collect(Collectors.groupingBy(like -> like.getComment().getId(), Collectors.counting()));
    }
}
