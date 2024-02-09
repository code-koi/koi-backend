package com.codekoi.apiserver.auth.service;

import com.codekoi.auth.usecase.CreateAuthTokenUseCase;
import com.codekoi.auth.usecase.DeleteAuthTokenUseCase;
import com.codekoi.auth.usecase.ValidateAuthTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final CreateAuthTokenUseCase createAuthTokenUseCase;
    private final DeleteAuthTokenUseCase deleteAuthTokenUseCase;
    private final ValidateAuthTokenUseCase validateAuthTokenUseCase;

    public void createAuthToken(Long userId, String refreshToken) {
        createAuthTokenUseCase.command(new CreateAuthTokenUseCase.Command(userId, refreshToken));
    }

    public void validateAuthToken(Long userId, String refreshToken) {
        validateAuthTokenUseCase.query(new ValidateAuthTokenUseCase.Query(userId, refreshToken));
    }

    public void deleteAuthToken(String refreshToken) {
        deleteAuthTokenUseCase.command(new DeleteAuthTokenUseCase.Command(refreshToken));
    }

}
