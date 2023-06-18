package codekoi.apiserver.utils.fixture;

import codekoi.apiserver.domain.koi.domain.KoiType;

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
}
