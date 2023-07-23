package com.codekoi.apiserver.auth.controller;

import com.codekoi.apiserver.user.service.UserQueryService;
import com.codekoi.coreweb.jwt.AuthInfo;
import com.codekoi.coreweb.jwt.JwtTokenProvider;
import com.codekoi.domain.authtoken.usecase.CreateAuthTokenUseCase;
import com.codekoi.domain.authtoken.usecase.DeleteAuthTokenUseCase;
import com.codekoi.domain.authtoken.usecase.ValidateAuthTokenUseCase;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import static com.codekoi.domain.authtoken.entity.AuthToken.REFRESH_TOKEN_VALID_DURATION;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserQueryService userQueryService;
    private final JwtTokenProvider jwtTokenProvider;

    private final CreateAuthTokenUseCase createAuthTokenUseCase;
    private final DeleteAuthTokenUseCase deleteAuthTokenUseCase;
    private final ValidateAuthTokenUseCase validateAuthTokenUseCase;


    //todo: oauth로 추후 전환하기.
    @PostMapping("/login")
    public void login(@RequestParam String email, HttpServletResponse response) {
        final Long userId = userQueryService.getUserIdByEmail(email);
        final AuthInfo authInfo = new AuthInfo(userId);
        createAccessTokenToResponse(response, authInfo);

        final String refreshToken = jwtTokenProvider.createRefreshToken();
        createAuthTokenUseCase.command(new CreateAuthTokenUseCase.Command(authInfo.getUserId(), refreshToken));
        setRefreshTokenInResponse(refreshToken, REFRESH_TOKEN_VALID_DURATION, response);
    }

    @PostMapping("/login/refresh")
    public void refresh(@CookieValue(value = "refreshToken") Cookie cookie,
                        HttpServletResponse response
    ) {
        final String refreshToken = cookie.getValue();
        jwtTokenProvider.validateExpiredRefreshToken(refreshToken);

        final AuthInfo authInfo = jwtTokenProvider.parseExpirableAccessToken(cookie.getValue());
        final Long userId = authInfo.getUserId();
        validateAuthTokenUseCase.query(new ValidateAuthTokenUseCase.Query(userId, refreshToken));

        createAccessTokenToResponse(response, authInfo);
        setRefreshTokenInResponse(refreshToken, REFRESH_TOKEN_VALID_DURATION, response);
    }

    @PostMapping("/logout")
    public void logout(@CookieValue(value = "refreshToken") Cookie cookie,
                       HttpServletResponse response) {
        final String refreshToken = cookie.getValue();
        deleteAuthTokenUseCase.command(new DeleteAuthTokenUseCase.Command(refreshToken));

        setRefreshTokenInResponse("", 0, response);
    }

    private void createAccessTokenToResponse(HttpServletResponse response, AuthInfo authInfo) {
        final String accessToken = jwtTokenProvider.createAccessToken(authInfo);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    }

    private void setRefreshTokenInResponse(String refreshToken, int maxAge, HttpServletResponse response) {
        final ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(maxAge)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}
