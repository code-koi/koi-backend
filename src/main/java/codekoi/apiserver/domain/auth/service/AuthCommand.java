package codekoi.apiserver.domain.auth.service;

import codekoi.apiserver.domain.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AuthCommand {

    private final AuthRepository authRepository;

    public void deleteUserAuth(String refreshToken) {
        authRepository.findByRefreshToken(refreshToken);
    }

}
