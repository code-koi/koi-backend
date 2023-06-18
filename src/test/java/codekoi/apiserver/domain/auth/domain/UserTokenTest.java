package codekoi.apiserver.domain.auth.domain;

import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.utils.EntityReflectionTestUtil;
import codekoi.apiserver.utils.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class UserTokenTest {

    @Test
    @DisplayName("UserToken의 유저정보와 요청한 userId가 다르면 false를 리턴한다.")
    void notMatchedUserId() {
        //given
        final User user = UserFixture.SUNDO.toUser();
        EntityReflectionTestUtil.setId(user, 1L);

        //when
        final UserToken userToken = UserToken.of(user, "refreshToken");

        //then
        assertThat(userToken.isUserMatched(2L)).isEqualTo(false);

    }

}