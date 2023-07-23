package com.codekoi.domain.user.service;

import com.codekoi.domain.user.entity.User;
import com.codekoi.domain.user.exception.UserNotFoundException;
import com.codekoi.domain.user.repository.UserCoreRepository;
import com.codekoi.domain.user.usecase.QueryUserByEmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueryUserByEmail implements QueryUserByEmailUseCase {

    private final UserCoreRepository userCoreRepository;

    @Override
    public User query(Query query) {
        final String email = query.email();
        return userCoreRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new UserNotFoundException(email);
                });
    }
}
