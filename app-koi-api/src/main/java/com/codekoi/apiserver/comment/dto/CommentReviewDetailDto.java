package com.codekoi.apiserver.comment.dto;

import com.codekoi.apiserver.user.dto.UserProfileDto;
import com.codekoi.coreweb.formatter.BeforeTimeSerializer;
import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.koi.KoiHistory;
import com.codekoi.domain.koi.KoiType;
import com.codekoi.domain.like.Like;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CommentReviewDetailDto {

    private UserProfileDto user;

    private Long id;

    @JsonSerialize(using = BeforeTimeSerializer.class)
    private LocalDateTime createdAt;

    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private KoiType koiType;

    private Boolean me;

    private long likeCount;

    private boolean liked;

    public CommentReviewDetailDto(UserProfileDto user, Long id, LocalDateTime createdAt, String content, KoiType koiType, Boolean me, long likeCount, boolean liked) {
        this.user = user;
        this.id = id;
        this.createdAt = createdAt;
        this.content = content;
        this.koiType = koiType;
        this.me = me;
        this.likeCount = likeCount;
        this.liked = liked;
    }

    public static List<CommentReviewDetailDto> listOf(List<ReviewComment> comment, List<KoiHistory> koiHistories, Long sessionUserId, List<Like> likes) {
        final Map<Long, KoiType> koiMap = getKoiMap(koiHistories);
        final Map<Long, Long> likeCountMap = getLikeCountMap(likes);
        final Map<Long, Boolean> likedByMeMap = getLikedByMeMap(likes, sessionUserId);

        return comment.stream()
                .map(c -> of(sessionUserId, koiMap, likeCountMap, likedByMeMap, c))
                .toList();
    }

    private static Map<Long, KoiType> getKoiMap(List<KoiHistory> koiHistories) {
        return koiHistories.stream()
                .collect(Collectors.toMap(koiHistory -> koiHistory.getCodeReviewComment().getId(),
                        KoiHistory::getKoiType));
    }

    private static Map<Long, Long> getLikeCountMap(List<Like> likes) {
        return likes.stream()
                .collect(Collectors.groupingBy(like -> like.getComment().getId(), Collectors.counting()));
    }

    private static CommentReviewDetailDto of(Long sessionUserId, Map<Long, KoiType> koiMap, Map<Long, Long> likeCountMap, Map<Long, Boolean> likedByMeMap, ReviewComment c) {
        return new CommentReviewDetailDto(
                UserProfileDto.from(c.getUser()),
                c.getId(),
                c.getCreatedAt(),
                c.getContent(),
                koiMap.get(c.getId()),
                Objects.equals(sessionUserId, c.getUser().getId()),
                likeCountMap.getOrDefault(c.getId(), 0L),
                likedByMeMap.getOrDefault(c.getId(), false)
        );
    }

    private static Map<Long, Boolean> getLikedByMeMap(List<Like> likes, Long userId) {
        return likes.stream()
                .filter(like -> like.getUser().getId().equals(userId))
                .collect(Collectors.toMap(Like::getId, like -> Boolean.TRUE));
    }
}
