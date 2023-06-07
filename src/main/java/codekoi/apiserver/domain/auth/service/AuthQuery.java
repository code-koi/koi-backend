package codekoi.apiserver.domain.auth.service;

import codekoi.apiserver.domain.auth.domain.UserToken;
import codekoi.apiserver.domain.auth.repository.AuthRepository;
import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthQuery {
    private final AuthRepository authRepository;

    public void validateUserRefreshToken(Long userId, String refreshToken) {
        final UserToken userToken = authRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> {
                    throw new InvalidValueException(ErrorInfo.TOKEN_INVALID_TYPE_ERROR);
                });

        if (!userToken.isUserMatched(userId)) {
            throw new InvalidValueException(ErrorInfo.TOKEN_NOT_MATCHED);
        }
    }
}
