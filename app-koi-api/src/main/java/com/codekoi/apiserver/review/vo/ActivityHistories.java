package com.codekoi.apiserver.review.vo;


import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.favorite.Favorite;
import com.codekoi.domain.like.Like;
import com.codekoi.domain.review.CodeReview;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ActivityHistories {

    private static final int HISTORY_SIZE = 10;

    public enum Type {
        REVIEW("리뷰 작성"), COMMENT("리뷰 요청"), LIKE("좋아요"), FAVORITE("즐겨찾기");

        Type(String s) {

        }
    }

    private final List<Activity> activities = new ArrayList<>();

    public ActivityHistories(List<CodeReview> codeReviews,
                             List<ReviewComment> comments,
                             List<Like> likes,
                             List<Favorite> favorites
    ) {
        activities.addAll(
                codeReviews.stream()
                        .map(r -> new Activity(Type.REVIEW, r.getId(), r.getTitle(), r.getCreatedAt()))
                        .toList()
        );

        activities.addAll(
                comments.stream()
                        .map(c -> new Activity(Type.COMMENT, c.getCodeReview().getId(), c.getContent(), c.getCreatedAt()))
                        .toList()
        );

        activities.addAll(
                likes.stream()
                        .map(l -> new Activity(Type.LIKE, l.getComment().getCodeReview().getId(), l.getComment().getContent(), l.getCreatedAt()))
                        .toList()
        );

        activities.addAll(
                favorites.stream()
                        .map(f -> new Activity(Type.FAVORITE, f.getCodeReview().getId(), f.getCodeReview().getTitle(), f.getCreatedAt()))
                        .toList()
        );

        activities.sort(Comparator.comparing(Activity::createdAt).reversed());
    }

    public List<Activity> getTopN() {
        return HISTORY_SIZE > activities.size() ? activities : activities.subList(0, HISTORY_SIZE);
    }
}
