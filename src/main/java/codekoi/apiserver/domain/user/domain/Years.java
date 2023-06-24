package codekoi.apiserver.domain.user.domain;

import codekoi.apiserver.domain.user.exception.InvalidUserYearsException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Years {

    private static final int MIN_YEAR = 0;
    private static final int MAX_YEAR = 21;

    @Column(name = "years")
    private int value;

    public Years(int years) {
        validate(years);
        this.value = years;
    }

    private void validate(int years) {
        if (years < MIN_YEAR || years > MAX_YEAR) {
            throw new InvalidUserYearsException();
        }
    }

    public String toYearString() {
        if (value == 0) {
            return "신입";
        } else if (1 <= value && value <= 20) {
            return value + "년차";
        }
        return "20년차 이상";
    }
}
