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

    private final List<Activity> activities = new ArrayList<>();

    public ActivityHistories(List<CodeReview> codeReviews,
                             List<ReviewComment> comments,
                             List<Like> likes,
                             List<Favorite> favorites
    ) {
        activities.addAll(
                codeReviews.stream()
                        .map(Activity::from)
                        .toList()
        );

        activities.addAll(
                comments.stream()
                        .map(Activity::from)
                        .toList()
        );

        activities.addAll(
                likes.stream()
                        .map(Activity::from)
                        .toList()
        );

        activities.addAll(
                favorites.stream()
                        .map(Activity::from)
                        .toList()
        );

        activities.sort(Comparator.comparing(Activity::createdAt).reversed());
    }

    public List<Activity> getTopN() {
        return HISTORY_SIZE > activities.size() ? activities : activities.subList(0, HISTORY_SIZE);
    }
}
