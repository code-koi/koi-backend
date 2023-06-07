package codekoi.apiserver.domain.auth.controller;

import codekoi.apiserver.global.token.JwtTokenProvider;
import codekoi.apiserver.domain.auth.service.AuthCommand;
import codekoi.apiserver.domain.auth.service.AuthQuery;
import codekoi.apiserver.domain.user.dto.UserAuth;
import codekoi.apiserver.domain.user.service.UserQuery;
import codekoi.apiserver.global.token.AuthenticationPrincipal;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserQuery userQuery;

    private final AuthQuery authQuery;
    private final AuthCommand authCommand;

    private final JwtTokenProvider jwtTokenProvider;

    private final int SEVEN_DAYS = 604800000;

    //todo: oauth로 추후 전환하기.
    @GetMapping("/login")
    public void login(@RequestParam String email, HttpServletResponse response) {
        final UserAuth userAuth = userQuery.getUserAuth(email);
        setAccessTokenInResponse(response, userAuth);

        final String newRefreshToken = jwtTokenProvider.createRefreshToken();
        setRefreshTokenInResponse(newRefreshToken, SEVEN_DAYS, response);
    }

    @PostMapping("/login/refresh")
    public void refresh(@CookieValue(value = "refreshToken") Cookie cookie,
                        @AuthenticationPrincipal UserAuth userAuth,
                        HttpServletResponse response
    ) {
        final String refreshToken = cookie.getValue();
        jwtTokenProvider.validateRefreshToken(refreshToken);

        final Long userId = userAuth.getUserId();
        authQuery.validateUserRefreshToken(userId, refreshToken);

        setAccessTokenInResponse(response, userAuth);
    }

    @PostMapping("/logout")
    public void logout(@CookieValue(value = "refreshToken") Cookie cookie,
                       HttpServletResponse response) {
        final String refreshToken = cookie.getValue();
        authCommand.deleteUserAuth(refreshToken);

        setRefreshTokenInResponse("", 0, response);
    }

    private void setAccessTokenInResponse(HttpServletResponse response, UserAuth userAuth) {
        final String accessToken = jwtTokenProvider.createAccessToken(userAuth);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    }

    private void setRefreshTokenInResponse(String refreshToken, int maxAge, HttpServletResponse response) {
        final ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(maxAge)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}
