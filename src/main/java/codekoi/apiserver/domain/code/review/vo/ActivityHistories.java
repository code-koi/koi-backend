package codekoi.apiserver.domain.code.review.vo;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.like.domain.Like;
import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.Favorite;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ActivityHistories {

    public enum Type {
        REVIEW("리뷰 작성"), COMMENT("리뷰 요청"), LIKE("좋아요"), FAVORITE("즐겨찾기");

        Type(String s) {

        }
    }

    private final List<Activity> activities = new ArrayList<>();

    public ActivityHistories(List<CodeReview> codeReviews,
                             List<CodeReviewComment> comments,
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
        int N = 10;
        if (N > activities.size()) {
            return activities;
        }
        return activities.subList(0, 10);
    }
}
