package com.codekoi.domain.authtoken.usecase;

public interface DeleteAuthTokenUseCase {

    void command(Command command);

    record Command(
            String refreshToken
    ) {

    }


}
