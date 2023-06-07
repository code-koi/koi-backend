package codekoi.apiserver.global.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorInfo {

    // COMMON_ERROR
    COMMON_RESOURCE_NOT_FOUND("해당 경로가 존재하지 않습니다.", "COMMON_001", BAD_REQUEST),
    COMMON_INTERNAL_SERVER_ERROR("알 수 없는 오류가 발생했습니다.", "COMMON_002", INTERNAL_SERVER_ERROR),
    COMMON_INVALID_INPUT_ERROR("유효하지 않은 입력입니다.", "COMMON_003", BAD_REQUEST),
    COMMON_BAD_REQUEST("잘못된 요청입니다.", "COMMON_004", NOT_FOUND),
    COMMON_NOT_FOUND("해당 리소스가 존재하지 않습니다.", "COMMON_005", NOT_FOUND),

    // AUTH_ERROR
    UNAUTHORIZED_USER_ERROR("로그인되지 않은 유저입니다. 로그인 해주세요", "AUTH_001", UNAUTHORIZED),
    TOKEN_NOT_EXIST_ERROR("토큰값이 존재하지 않습니다.", "AUTH_002", BAD_REQUEST),
    TOKEN_INVALID_TYPE_ERROR("식별되지 않는 토큰입니다.", "AHTH_003", BAD_REQUEST),
    TOKEN_NOT_MATCHED("변조된 토큰입니다.", "AUTH_004", BAD_REQUEST),
    TOKEN_EXPIRED("만료된 토큰입니다.", "AUTH_005", BAD_REQUEST),


    // USER_ERROR
    USER_NOT_FOUND_ERROR("해당 유저를 찾을 수 없습니다.", "USER_001", BAD_REQUEST),
    USER_INTRODUCE_OVER_ERROR("유저의 자기소개는 1200자미만이어야 합니다.", "USER_002", BAD_REQUEST),
    USER_YEARS_ERROR("유저의 경력은 0 ~ 20년 사이만 가능합니다.", "USER_003", BAD_REQUEST),
    USER_EMAIL_ERROR("유저의 이메일 형식이 올바르지 않습니다", "USER_004", BAD_REQUEST);


    private String message;
    private String code;
    private final HttpStatus statusCode;

    ErrorInfo(String message, String code, HttpStatus statusCode) {
        this.message = message;
        this.code = code;
        this.statusCode = statusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
