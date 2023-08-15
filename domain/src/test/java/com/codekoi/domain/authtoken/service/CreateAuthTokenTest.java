package com.codekoi.domain.authtoken.service;


import com.codekoi.domain.authtoken.AuthToken;
import com.codekoi.domain.authtoken.AuthTokenRepository;
import com.codekoi.domain.authtoken.usecase.CreateAuthTokenUseCase;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.codekoi.fixture.UserFixture.HONG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateAuthTokenTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthTokenRepository authTokenRepository;

    @InjectMocks
    private CreateAuthToken CreateAuthToken;

    @Captor
    ArgumentCaptor<AuthToken> authTokenCaptor;


    @Test
    void 토큰_생성에_성공한다() {
        //given
        final User user = HONG.toUser(1L);
        given(userRepository.getOneById(any()))
                .willReturn(user);

        final CreateAuthTokenUseCase.Command command = new CreateAuthTokenUseCase.Command(1L, "123");

        //when
        final Long id = CreateAuthToken.command(command);

        //then
        verify(authTokenRepository).save(authTokenCaptor.capture());

        final AuthToken authToken = authTokenCaptor.getValue();
        assertThat(authToken.getUser()).isEqualTo(user);
    }
}