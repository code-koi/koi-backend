package codekoi.apiserver.domain.auth.service;

import codekoi.apiserver.domain.auth.domain.UserToken;
import codekoi.apiserver.domain.auth.repository.UserTokenRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.repository.UserRepository;
import codekoi.apiserver.global.token.exception.InvalidTokenTypeException;
import codekoi.apiserver.global.token.exception.RefreshTokenNotMatchedException;
import codekoi.apiserver.utils.ServiceTest;
import codekoi.apiserver.utils.fixture.UserFixture;
import codekoi.apiserver.utils.fixture.UserTokenFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserTokenQueryTest extends ServiceTest {

    @Autowired
    UserTokenQuery userTokenQuery;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;

    @DisplayName("refreshToken에 대한 userToken이 없어서 예외가 발생한다.")
    @Test
    void noRefreshTokenInUserToken() {
        //then
        assertThatThrownBy(() -> {
            //when
            userTokenQuery.validateUserRefreshToken(1L, "REFRESH_TOKEN");
        }).isInstanceOf(InvalidTokenTypeException.class);
    }

    @DisplayName("userToken과 요청한 유저의 id가 다르면 예외가 발생한다.")
    @Test
    void notMatchedWithUser() {
        //given
        final User user = UserFixture.SUNDO.toUser();
        final UserToken userToken = UserTokenFixture.VALID_TOKEN.toUserToken(user);

        userRepository.save(user);
        userTokenRepository.save(userToken);

        //when
        assertThatThrownBy(() -> {
            userTokenQuery.validateUserRefreshToken(user.getId() + 1, userToken.refreshToken);
        }).isInstanceOf(RefreshTokenNotMatchedException.class);
    }
}