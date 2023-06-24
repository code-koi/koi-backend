package codekoi.apiserver.domain.user.domain;


import codekoi.apiserver.domain.user.exception.InvalidUserIntroduceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IntroduceTest {
    static Stream<Arguments> invalidIntroduce() {
        return Stream.of(
                Arguments.of("A".repeat(1025))
        );
    }

    @DisplayName("유저의 자기소개가 비정상적으로 입력된다.")
    @ParameterizedTest(name = "{0}로 자기소개가 입력된 경우, 예외가 발생한다.")
    @MethodSource("invalidIntroduce")
    @NullSource
    void invalidLength(String input) {
        assertThatThrownBy(() -> {
            final Introduce introduce = new Introduce(input);
        }).isInstanceOf(InvalidUserIntroduceException.class);
    }
}