package com.codekoi.auth;

import com.codekoi.auth.exception.RefreshTokenNotAppliedException;
import com.codekoi.auth.exception.RefreshTokenNotMatchedException;
import com.codekoi.auth.usecase.ValidateAuthTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class ValidateAuthToken implements ValidateAuthTokenUseCase {

    private final AuthTokenRepository authTokenRepository;

    @Override
    public void query(Query query) {
        final AuthToken authToken = authTokenRepository.findByRefreshToken(query.refreshToken())
                .orElseThrow(RefreshTokenNotAppliedException::new);

        if (!authToken.isUserMatched(query.userId())) {
            throw new RefreshTokenNotMatchedException();
        }
    }
}
