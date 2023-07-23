package com.codekoi.coreweb.jwt;

public class AuthInfo {
    private Long userId;

    public AuthInfo(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
