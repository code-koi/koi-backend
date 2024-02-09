package com.codekoi.user.usecase;

import com.codekoi.user.User;

public interface QueryUserByEmailUseCase {

    User query(Query query);

    record Query(
            String email
    ) {
    }

}
