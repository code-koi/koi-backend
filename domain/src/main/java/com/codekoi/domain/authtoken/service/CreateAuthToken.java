package com.codekoi.domain.authtoken.service;

import com.codekoi.domain.authtoken.entity.AuthToken;
import com.codekoi.domain.authtoken.repository.AuthTokenCoreRepository;
import com.codekoi.domain.authtoken.usecase.CreateAuthTokenUseCase;
import com.codekoi.domain.user.entity.User;
import com.codekoi.domain.user.repository.UserCoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateAuthToken implements CreateAuthTokenUseCase {

    private final UserCoreRepository userCoreRepository;
    private final AuthTokenCoreRepository authTokenCoreRepository;

    @Override
    public Long command(Command command) {
        final User user = userCoreRepository.getOneById(command.userId());

        final AuthToken authToken = AuthToken.of(user, command.refreshToken());
        authTokenCoreRepository.save(authToken);

        return authToken.getId();
    }
}
