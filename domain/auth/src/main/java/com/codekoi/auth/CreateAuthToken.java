package com.codekoi.auth;

import com.codekoi.auth.usecase.CreateAuthTokenUseCase;
import com.codekoi.user.User;
import com.codekoi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class CreateAuthToken implements CreateAuthTokenUseCase {

    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;

    @Override
    public Long command(Command command) {
        final User user = userRepository.getOneById(command.userId());

        final AuthToken authToken = AuthToken.of(user, command.refreshToken());
        authTokenRepository.save(authToken);

        return authToken.getId();
    }
}
