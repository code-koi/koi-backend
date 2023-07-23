package com.codekoi.domain.authtoken.service;

import com.codekoi.domain.authtoken.repository.AuthTokenCoreRepository;
import com.codekoi.domain.authtoken.usecase.DeleteAuthTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteAuthToken implements DeleteAuthTokenUseCase {

    private final AuthTokenCoreRepository authTokenCoreRepository;

    @Override
    public void command(Command command) {
        final String refreshToken = command.refreshToken();
        authTokenCoreRepository.deleteByRefreshToken(refreshToken);
    }
}
