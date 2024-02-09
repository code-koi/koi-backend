package com.codekoi.auth;

import com.codekoi.auth.usecase.DeleteAuthTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class DeleteAuthToken implements DeleteAuthTokenUseCase {

    private final AuthTokenRepository authTokenRepository;

    @Override
    public void command(Command command) {
        final String refreshToken = command.refreshToken();
        authTokenRepository.deleteByRefreshToken(refreshToken);
    }
}
