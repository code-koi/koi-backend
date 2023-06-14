package codekoi.apiserver.global.token;

import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private final String accessTokenKey = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa=";
    private final String refreshTokenKey = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb=";

    private final int accessTokenValidMilliseconds = 300000;
    private final int refreshTokenValidMilliseconds = 604800000;

    final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            accessTokenValidMilliseconds,
            refreshTokenValidMilliseconds,
            accessTokenKey,
            refreshTokenKey);

    final UserToken userToken = new UserToken(1L);

    @Test
    @DisplayName("User 정보를 통해서 토큰을 다시 파싱하면 같은 결과가 나온다.")
    void createAccessToken() {
        //given
        final String accessToken = jwtTokenProvider.createAccessToken(userToken);

        //when
        final UserToken parsedUserToken = jwtTokenProvider.parseByAccessToken(accessToken);

        //then
        assertThat(userToken).usingRecursiveComparison()
                .isEqualTo(parsedUserToken);
    }

    @Test
    @DisplayName("refresh토큰의 유효시간이 지난 경우 예외가 발생한다.")
    void overTimeRefreshToken() {
        //given
        final JwtTokenProvider shortValidTokenProvider = new JwtTokenProvider(
                10,
                10,
                accessTokenKey,
                refreshTokenKey);

        final String accessToken = shortValidTokenProvider.createAccessToken(userToken);

        //then
        assertThatThrownBy(() -> {
            //when
            shortValidTokenProvider.parseByAccessToken(accessToken);
        }).isInstanceOf(InvalidValueException.class)
                .extracting("errorInfo")
                .isEqualTo(ErrorInfo.TOKEN_EXPIRED);
    }

    static Stream<Arguments> invalidToken() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("B ")
        );
    }

    @MethodSource("invalidToken")
    @ParameterizedTest(name = "{0}인 경우 토큰형식에 맞지 않아 예외가 발생한다.")
    @NullSource
    void invalidTokenType(String token) {
        assertThatThrownBy(() -> {
            jwtTokenProvider.parseByAccessToken(token);
        }).isInstanceOf(InvalidValueException.class)
                .extracting("errorInfo")
                .isEqualTo(ErrorInfo.TOKEN_EXPIRED);

    }
}