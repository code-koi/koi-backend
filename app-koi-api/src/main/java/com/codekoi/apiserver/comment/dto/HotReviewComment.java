package com.codekoi.apiserver.comment.dto;

import com.codekoi.apiserver.user.dto.UserProfileDto;
import com.codekoi.koi.KoiHistory;
import com.codekoi.koi.KoiType;
import com.codekoi.review.CommentLike;
import com.codekoi.review.ReviewComment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
public class HotReviewComment {

    private Long id;

    private UserProfileDto user;

    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private KoiType koiType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean liked;

    private Integer likeCount;

    public HotReviewComment(Long id, UserProfileDto user, String content, KoiType koiType, Boolean liked, Integer likeCount) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.koiType = koiType;
        this.liked = liked;
        this.likeCount = likeCount;
    }

    public static List<HotReviewComment> listOf(List<ReviewComment> comments, List<KoiHistory> koiHistories, List<CommentLike> likesByMe) {
        final Map<Long, KoiType> koiMap = koiHistories.stream()
                .collect(Collectors.toMap(koiHistory -> koiHistory.getCodeReviewComment().getId(), KoiHistory::getKoiType));

        final Map<Long, CommentLike> likeMap = likesByMe.stream()
                .collect(Collectors.toMap(like -> like.getComment().getId(), like -> like));

        return comments.stream()
                .map(c -> {
                    final Long commentId = c.getId();
                    final Long reviewId = c.getCodeReview().getId();

                    final Boolean isLikedByMe = likeMap.get(commentId) == null ? null : true;

                    return of(koiMap, c, commentId, reviewId, isLikedByMe);
                }).toList();
    }

    private static HotReviewComment of(Map<Long, KoiType> koiMap, ReviewComment c, Long commentId, Long reviewId, Boolean isLikedByMe) {
        return new HotReviewComment(
                reviewId,
                UserProfileDto.from(c.getUser()),
                c.getContent(),
                koiMap.get(commentId),
                isLikedByMe,
                c.getCommentLikes().size()
        );
    }
}
