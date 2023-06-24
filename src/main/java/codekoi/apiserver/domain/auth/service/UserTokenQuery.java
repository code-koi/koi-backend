package codekoi.apiserver.domain.auth.service;

import codekoi.apiserver.domain.auth.domain.UserToken;
import codekoi.apiserver.domain.auth.repository.UserTokenRepository;
import codekoi.apiserver.global.token.exception.InvalidTokenTypeException;
import codekoi.apiserver.global.token.exception.RefreshTokenNotMatchedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserTokenQuery {
    private final UserTokenRepository userTokenRepository;

    public void validateUserRefreshToken(Long userId, String refreshToken) {
        final UserToken userToken = userTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(InvalidTokenTypeException::new);

        if (!userToken.isUserMatched(userId)) {
            throw new RefreshTokenNotMatchedException();
        }
    }
}
