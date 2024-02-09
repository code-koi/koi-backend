package com.codekoi.user.service;

import com.codekoi.user.User;
import com.codekoi.user.UserRepository;
import com.codekoi.user.exception.UserNotFoundException;
import com.codekoi.user.usecase.QueryUserByEmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class QueryUserByEmail implements QueryUserByEmailUseCase {

    private final UserRepository userRepository;

    @Override
    public User query(Query query) {
        final String email = query.email();
        return userRepository.findByEmail(email).orElseThrow(() -> {
            throw new UserNotFoundException(email);
        });
    }
}
