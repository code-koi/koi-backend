package com.codekoi.domain.authtoken.service;

import com.codekoi.domain.authtoken.entity.AuthToken;
import com.codekoi.domain.authtoken.exception.RefreshTokenNotAppliedException;
import com.codekoi.domain.authtoken.exception.RefreshTokenNotMatchedException;
import com.codekoi.domain.authtoken.repository.AuthTokenCoreRepository;
import com.codekoi.domain.authtoken.usecase.ValidateAuthTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ValidateAuthToken implements ValidateAuthTokenUseCase {

    private final AuthTokenCoreRepository authTokenCoreRepository;

    @Override
    public void query(Query query) {
        final AuthToken authToken = authTokenCoreRepository.findByRefreshToken(query.refreshToken())
                .orElseThrow(RefreshTokenNotAppliedException::new);

        if (!authToken.isUserMatched(query.userId())) {
            throw new RefreshTokenNotMatchedException();
        }
    }
}
