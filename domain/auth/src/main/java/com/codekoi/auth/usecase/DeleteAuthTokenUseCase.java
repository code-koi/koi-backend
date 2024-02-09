package com.codekoi.auth.usecase;

public interface DeleteAuthTokenUseCase {

    void command(Command command);

    record Command(
            String refreshToken
    ) {

    }


}
