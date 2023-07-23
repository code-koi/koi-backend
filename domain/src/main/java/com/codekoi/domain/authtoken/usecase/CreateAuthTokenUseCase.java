package com.codekoi.domain.authtoken.usecase;

public interface CreateAuthTokenUseCase {

    Long command(Command command);

    record Command(
            Long userId,
            String refreshToken
    ) {
    }

}
