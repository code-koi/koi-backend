package codekoi.apiserver.domain.auth.service;

import codekoi.apiserver.domain.auth.domain.UserToken;
import codekoi.apiserver.domain.auth.repository.UserTokenRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserTokenCommand {

    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    public void deleteUserToken(String refreshToken) {
        userTokenRepository.deleteByRefreshToken(refreshToken);
    }

    public void createUserToken(Long userId, String refreshToken) {
        final User user = userRepository.findByUserId(userId);

        final UserToken userToken = UserToken.of(user, refreshToken);
        userTokenRepository.save(userToken);
    }
}
