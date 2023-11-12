package com.codekoi.error;

import lombok.Getter;


@Getter
public enum ErrorInfo {

    // COMMON_ERROR
    COMMON_RESOURCE_NOT_FOUND("해당 경로가 존재하지 않습니다.", "COMMON_001", 400),
    COMMON_INTERNAL_SERVER_ERROR("알 수 없는 오류가 발생했습니다.", "COMMON_002", 500),
    COMMON_INVALID_INPUT_ERROR("유효하지 않은 입력입니다.", "COMMON_003", 400),
    COMMON_BAD_REQUEST("잘못된 요청입니다.", "COMMON_004", 404),
    COMMON_NOT_FOUND("해당 리소스가 존재하지 않습니다.", "COMMON_005", 404),

    // AUTH_ERROR
    TOKEN_UNAUTHORIZED_USER("로그인되지 않은 유저입니다. 로그인 해주세요", "AUTH_001", 401),
    TOKEN_NOT_EXIST("토큰값이 존재하지 않습니다.", "AUTH_002", 401),
    TOKEN_INVALID_TYPE("식별되지 않는 토큰입니다.", "AUTH_003", 401),
    TOKEN_NOT_MATCHED("다른 유저의 토큰입니다.", "AUTH_004", 401),
    TOKEN_EXPIRED_ACCESS_TOKEN("만료된 Access 토큰입니다. 토큰을 재활성화 해주세요.", "AUTH_005", 401),
    TOKEN_EXPIRED_REFRESH_TOKEN("만료된 Refresh 토큰입니다. 토큰을 재활성화 해주세요.", "AUTH_006", 401),
    TOKEN_REFRESH_TOKEN_NOT_APPLIED("등록되지 않은 Refresh 토큰입니다.", "AUTH_007", 400),

    // USER_ERROR
    USER_NOT_FOUND("해당 유저를 찾을 수 없습니다.", "USER_001", 400),
    USER_INTRODUCE_OVER("유저의 자기소개는 1200자미만이어야 합니다.", "USER_002", 400),
    USER_YEARS_INVALID("유저의 경력은 0 ~ 20년 사이만 가능합니다.", "USER_003", 400),
    USER_EMAIL_INVALID("유저의 이메일 형식이 올바르지 않습니다", "USER_004", 400),
    USER_NICKNAME_OVER("유저의 닉네임은 10자 이하만 가능합니다.", "USER_005", 400),

    CODE_REVIEW_NOT_FOUND("해당 코드리뷰 요청건을 찾을 수 없습니다.", "REVIEW_001", 400),
    CAN_NOT_DELETE_CODE_REVIEW("코드 리뷰를 삭제할 권한이 없습니다.", "REVIEW_002", 403),

    COMMENT_NOT_FOUND("해당 답변을 찾을 수 없습니다.", "COMMENT_001", 400),

    // COMMENT_LIKE
    LIKE_NOT_FOUND("해당 좋아요를 찾을 수 없습니다.", "LIKE_001", 400),
    LIKE_USER_NOT_MATCHED("좋아요를 한 유저와 매칭되지 않는 세션입니다.", "LIKE_002", 400),
    ALREADY_LIKED_COMMENT("이미 좋아요한 답변입니다.", "LIKE_003", 400),
    NOT_LIKED_COMMENT("좋아요 하지 않은 답변입니다.", "LIKE_004", 400),
    ;


    private String message;
    private String code;
    private final int statusCode;

    ErrorInfo(String message, String code, int statusCode) {
        this.message = message;
        this.code = code;
        this.statusCode = statusCode;
    }

}
