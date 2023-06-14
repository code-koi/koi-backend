package codekoi.apiserver.domain.user.domain;

import codekoi.apiserver.global.error.exception.DomainLogicException;
import codekoi.apiserver.global.error.exception.ErrorInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class YearsTest {

    static Stream<Arguments> invalidYears() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(22)
        );
    }

    @DisplayName("유저의 경력이 유효하지 않는 테스트")
    @ParameterizedTest(name = "{0}의 경력이 입력되면 예외가 발생한다.")
    @MethodSource("invalidYears")
    void invalidYearsTest(int years) {
        assertThatThrownBy(() -> {
            new Years(years);
        }).isInstanceOf(DomainLogicException.class)
                .extracting("errorInfo")
                .isEqualTo(ErrorInfo.USER_YEARS_ERROR);
    }


    static Stream<Arguments> validYears() {
        return Stream.of(
                Arguments.of(0, "신입"),
                Arguments.of(1, "1년차"),
                Arguments.of(21, "20년차 이상")
        );
    }

    @DisplayName("유저의 경력을 문자열 형태로 치환한다.")
    @ParameterizedTest(name = "{0}로 입력 시, {1}로 변환된다.")
    @MethodSource("validYears")
    void toYearsString(int input, String output) {
        final Years years = new Years(input);
        assertThat(years.toYearString()).isEqualTo(output);
    }
}