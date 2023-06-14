
package codekoi.apiserver.global.error;

import codekoi.apiserver.global.error.exception.ErrorInfo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BasicErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String code;
    private int statusCode;

    private BasicErrorResponse(ErrorInfo errorInfo) {
        this.timestamp = LocalDateTime.now();
        this.message = errorInfo.getMessage();
        this.statusCode = errorInfo.getStatusCode().value();
        this.code = errorInfo.getCode();
    }

    private BasicErrorResponse(ErrorInfo errorInfo, String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.statusCode = errorInfo.getStatusCode().value();
        this.code = errorInfo.getCode();
    }

    public static BasicErrorResponse of(ErrorInfo error) {
        return new BasicErrorResponse(error);
    }

    public static BasicErrorResponse of(ErrorInfo error, String message) {
        return new BasicErrorResponse(error, message);
    }

}
