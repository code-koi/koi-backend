package com.codekoi.domain.authtoken.usecase;

public interface ValidateAuthTokenUseCase {

    void query(Query query);

    record Query(
            Long userId,
            String refreshToken
    ) {
    }
}
