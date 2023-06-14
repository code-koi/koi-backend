package codekoi.apiserver.domain.user.domain;

import codekoi.apiserver.global.error.exception.DomainLogicException;
import codekoi.apiserver.global.error.exception.ErrorInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class NicknameTest {

    static Stream<Arguments> invalidLength() {
        return Stream.of(
                Arguments.of("A".repeat(2)),
                Arguments.of("A".repeat(21)),
                Arguments.of(""),
                Arguments.of(" ".repeat(5))
        );
    }

    @DisplayName("입력값의 길이가 2 ~ 20 밖이거나 입력값이 Null이나 공백으로만 이루어진 문자열인 경우 실패")
    @ParameterizedTest(name = "{0}로 유저 이름 생성시 예외가 발생한다.")
    @NullSource
    @MethodSource("invalidLength")
    void createLengthFail(String nickname) {
        assertThatThrownBy(() -> new Nickname(nickname))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorInfo")
                .isEqualTo(ErrorInfo.USER_NICKNAME_OVER);
    }

    @DisplayName("입력값이 올바른 경우 성공")
    @Test
    void success() {
        final Nickname nickname = new Nickname("김선도");
        assertThat(nickname.getValue()).isEqualTo("김선도");
    }

}