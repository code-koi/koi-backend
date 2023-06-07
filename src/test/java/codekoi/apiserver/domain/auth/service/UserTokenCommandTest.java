package codekoi.apiserver.domain.auth.service;

import codekoi.apiserver.domain.auth.domain.UserToken;
import codekoi.apiserver.domain.auth.repository.UserTokenRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.repository.UserRepository;
import codekoi.apiserver.utils.fixture.UserFixture;
import codekoi.apiserver.utils.fixture.UserTokenFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class UserTokenCommandTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private UserTokenCommand userTokenCommand;

    @Test
    @DisplayName("refreshToken으로 UserToken을 삭제한다.")
    void deleteByRefreshToken() {
        //given
        final User user = UserFixture.SUNDO.toUser();
        userRepository.save(user);

        final UserToken userToken = UserTokenFixture.VALID_TOKEN.toUserToken(user);
        userTokenRepository.save(userToken);

        final String refreshToken = userToken.refreshToken;

        //when
        userTokenCommand.deleteUserToken(refreshToken);

        //then
        final Optional<UserToken> optional = userTokenRepository.findByRefreshToken(refreshToken);
        assertThat(optional).isEmpty();
    }

    @Test
    @DisplayName("유저의 토큰 정보를 저장한다")
    void saveUserTokenInfo() {
        //given
        final User user = UserFixture.SUNDO.toUser();
        userRepository.save(user);

        //when
        userTokenCommand.createUserToken(user.getId(), "REFRESH_TOKEN");

        //then
        final UserToken userToken = userTokenRepository.findByRefreshToken("REFRESH_TOKEN").get();

        assertThat(userToken.getUser().getId()).isEqualTo(user.getId());
        assertThat(userToken.getRefreshToken()).isEqualTo("REFRESH_TOKEN");
    }

}