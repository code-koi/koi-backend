package com.codekoi.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("userId: " + userId + " 유저를 찾을 수 없습니다.");
    }

    public UserNotFoundException(String email) {
        super("email: " + email + "에 맞는 유저가 존재하지 않습니다.");
    }
}
