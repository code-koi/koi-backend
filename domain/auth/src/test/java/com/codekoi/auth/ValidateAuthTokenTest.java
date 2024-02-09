package com.codekoi.auth;

import com.codekoi.auth.exception.RefreshTokenNotAppliedException;
import com.codekoi.auth.usecase.ValidateAuthTokenUseCase;
import com.codekoi.user.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.codekoi.auth.fixture.AuthTokenFixture.VALID_TOKEN;
import static com.codekoi.user.fixture.UserFixture.SUNDO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ValidateAuthTokenTest {

    @Mock
    AuthTokenRepository authTokenRepository;

    @InjectMocks
    ValidateAuthToken validateAuthToken;

    @Test
    void 토큰정보의_유저와_요청한_유저가_매칭되지_않아_예외가_발생한다() {
        //given
        final User user = SUNDO.toUser(1L);
        given(authTokenRepository.findByRefreshToken(any()))
                .willReturn(Optional.empty());

        String refreshToken = "123";
        final ValidateAuthTokenUseCase.Query query = new ValidateAuthTokenUseCase.Query(1L, refreshToken);

        //then
        assertThatThrownBy(() -> {
            //when
            validateAuthToken.query(query);
        }).isInstanceOf(RefreshTokenNotAppliedException.class);
    }

    @Test
    void 토큰_인증에_성공한다() {
        //given
        final User user = SUNDO.toUser(1L);
        given(authTokenRepository.findByRefreshToken(any()))
                .willReturn(Optional.of(VALID_TOKEN.toAuthToken(user)));

        String refreshToken = "123";
        final ValidateAuthTokenUseCase.Query query = new ValidateAuthTokenUseCase.Query(1L, refreshToken);

        //when
        validateAuthToken.query(query);
    }
}