package com.codekoi.fixture;


import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.koi.KoiHistory;
import com.codekoi.domain.koi.KoiType;
import com.codekoi.domain.user.User;

public enum KoiHistoryFixture {
    FISHBOWL(KoiType.FISHBOWL, "0원짜리 코이를 남깁니다."),
    STREAM(KoiType.STREAM, "3000원짜리 코이를 남깁니다."),
    RIVER(KoiType.RIVER, "40000원짜리 코이를 남깁니다."),
    SEA(KoiType.SEA, "5000원짜리 코이를 남깁니다."),

    ;

    public final KoiType koiType;
    public final String message;

    KoiHistoryFixture(KoiType koiType, String message) {
        this.koiType = koiType;
        this.message = message;
    }

    public KoiHistory toKoiHistory(Long id, User target, User source, ReviewComment reviewComment) {
        return KoiHistory.builder()
                .id(id)
                .target(target)
                .source(source)
                .koiType(koiType)
                .codeReviewComment(reviewComment)
                .build();
    }
}
