package com.codekoi.domain.authtoken.service;

import com.codekoi.domain.authtoken.AuthTokenRepository;
import com.codekoi.domain.authtoken.usecase.DeleteAuthTokenUseCase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeleteAuthTokenTest {

    @Mock
    private AuthTokenRepository authTokenRepository;

    @InjectMocks
    private DeleteAuthToken deleteAuthToken;

    @Test
    void 토큰_삭제에_성공한다() {
        //given
        String refreshToken = "123";
        final DeleteAuthTokenUseCase.Command command = new DeleteAuthTokenUseCase.Command(refreshToken);

        //when
        deleteAuthToken.command(command);

        //then
        verify(authTokenRepository, times(1)).deleteByRefreshToken(refreshToken);
    }
}