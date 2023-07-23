package com.codekoi.error;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BasicErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String errorCode;
    private int statusCode;

    private BasicErrorResponse(ErrorInfo errorInfo) {
        this.timestamp = LocalDateTime.now();
        this.message = errorInfo.getMessage();
        this.statusCode = errorInfo.getStatusCode();
        this.errorCode = errorInfo.getCode();
    }

    private BasicErrorResponse(ErrorInfo errorInfo, String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.statusCode = errorInfo.getStatusCode();
        this.errorCode = errorInfo.getCode();
    }

    public static BasicErrorResponse of(ErrorInfo error) {
        return new BasicErrorResponse(error);
    }

    public static BasicErrorResponse of(ErrorInfo error, String message) {
        return new BasicErrorResponse(error, message);
    }


}
