package codekoi.apiserver.domain.user.service;

import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.domain.user.repository.UserRepository;
import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;
import codekoi.apiserver.utils.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserQueryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserQuery userQuery;

    @Test
    @DisplayName("유저 조회 후, jwt로 생성할 dto로 변환한다.")
    void userAuthInfo() {
        //given
        final User user = UserFixture.SUNDO.toUser();
        userRepository.save(user);

        //when
        final UserToken userToken = userQuery.getUserAuth(user.getEmail());

        //then
        assertThat(userToken.getUserId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("이메일에 맞는 유저가 없으면 예외가 발생한다.")
    void hasNoUserEmail() {

        //then
        assertThatThrownBy(() -> {
            //when
            final UserToken userToken = userQuery.getUserAuth("random@abc.com");
        }).isInstanceOf(InvalidValueException.class)
                .extracting("errorInfo")
                .isEqualTo(ErrorInfo.USER_NOT_FOUND_ERROR);
    }
}