package com.codekoi.coreweb.jwt;

import com.codekoi.coreweb.jwt.exception.AccessTokenExpiredException;
import com.codekoi.coreweb.jwt.exception.InvalidTokenTypeException;
import com.codekoi.coreweb.jwt.exception.RefreshTokenExpiredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    final JwtTokenProvider shortTimeTokenProvider = new JwtTokenProvider(
            1,
            1,
            accessTokenKey,
            refreshTokenKey);

    final AuthInfo userToken = new AuthInfo(1L);

    @Nested
    @DisplayName("AccessToken 테스트")
    class AccessTokenTest {

        @Test
        @DisplayName("User 정보를 통해서 토큰을 다시 파싱하면 같은 결과가 나온다.")
        void createAccessToken() {
            //given
            final String accessToken = jwtTokenProvider.createAccessToken(userToken);

            //when
            final AuthInfo parsedAuthInfo = jwtTokenProvider.parseAccessToken(accessToken);

            //then
            assertThat(userToken).usingRecursiveComparison()
                    .isEqualTo(parsedAuthInfo);
        }

        @Test
        @DisplayName("accessToken의 유효시간이 지난 경우 예외가 발생한다.")
        void overTimeRefreshToken() {
            //given
            final String accessToken = shortTimeTokenProvider.createAccessToken(userToken);

            //then
            assertThatThrownBy(() -> {
                //when
                shortTimeTokenProvider.parseAccessToken(accessToken);
            }).isInstanceOf(AccessTokenExpiredException.class);
        }

        @Test
        @DisplayName("만료된 accessToken도 파싱된다.")
        void overTimeButParsed() {
            //given
            final String accessToken = shortTimeTokenProvider.createAccessToken(userToken);

            //when
            final AuthInfo authInfo = shortTimeTokenProvider.parseExpirableAccessToken(accessToken);

            //then
            assertThat(authInfo.getUserId()).isEqualTo(userToken.getUserId());
        }
    }


    @Nested
    @DisplayName("refreshToken 테스트")
    class RefreshTokenTest {
        @Test
        @DisplayName("refreshToken의 형식이 이상하면 예외가 발생한다.")
        void invalidRefreshToken() {
            //given
            String refreshToken = "a";

            //then
            assertThatThrownBy(() -> {
                //when
                jwtTokenProvider.validateExpiredRefreshToken(refreshToken);
            }).isInstanceOf(InvalidTokenTypeException.class);
        }

        @Test
        @DisplayName("refreshToken이 만료되면 예외가 발생한다.")
        void expiredRefreshToken() {
            //given
            final String refreshToken = shortTimeTokenProvider.createRefreshToken();

            //then
            assertThatThrownBy(() -> {
                //when
                shortTimeTokenProvider.validateExpiredRefreshToken(refreshToken);
            }).isInstanceOf(RefreshTokenExpiredException.class);
        }
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
        assertThatThrownBy(() -> jwtTokenProvider.parseAccessToken(token)).isInstanceOf(InvalidTokenTypeException.class);
    }

}