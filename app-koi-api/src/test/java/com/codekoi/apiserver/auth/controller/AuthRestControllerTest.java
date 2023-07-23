package com.codekoi.apiserver.auth.controller;

import com.codekoi.apiserver.user.service.UserQueryService;
import com.codekoi.apiserver.utils.ControllerTest;
import com.codekoi.coreweb.jwt.AuthInfo;
import com.codekoi.coreweb.jwt.JwtTokenProvider;
import com.codekoi.domain.authtoken.usecase.CreateAuthTokenUseCase;
import com.codekoi.domain.authtoken.usecase.DeleteAuthTokenUseCase;
import com.codekoi.domain.authtoken.usecase.ValidateAuthTokenUseCase;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = AuthRestController.class)
class AuthRestControllerTest extends ControllerTest {

    @MockBean
    UserQueryService userQueryService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    CreateAuthTokenUseCase createAuthTokenUseCase;
    @MockBean
    DeleteAuthTokenUseCase deleteAuthTokenUseCase;

    @MockBean
    ValidateAuthTokenUseCase validateAuthTokenUseCase;


    @DisplayName("로그인 시 accessToken과 refreshToken을 발급한다")
    @Test
    void successLogin() throws Exception {
        //given
        given(userQueryService.getUserIdByEmail(anyString()))
                .willReturn(1L);

        given(jwtTokenProvider.createAccessToken(any()))
                .willReturn(accessToken);
        given(jwtTokenProvider.createRefreshToken())
                .willReturn(refreshToken);

        //when
        final ResultActions result = mvc.perform(post("/api/login")
                .queryParam("email", "sdcodebase@gmail.com")
        );

        //then
        result.andDo(print())
                .andDo(loginApiDocs())
                .andExpect(status().isOk())
                .andExpect(header().string(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(cookie().httpOnly("refreshToken", true))
                .andExpect(cookie().maxAge("refreshToken", 604800000))
                .andExpect(cookie().sameSite("refreshToken", "None"));

    }

    @DisplayName("refreshToken을 이용해서 accessToken을 발급한다.")
    @Test
    void createNewAccessTokenByRefreshToken() throws Exception {
        //given
        given(jwtTokenProvider.parseExpirableAccessToken(any()))
                .willReturn(new AuthInfo(1L));

        given(jwtTokenProvider.createAccessToken(any()))
                .willReturn("NEW_ACCESS_TOKEN");

        final Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        //when
        final ResultActions result = mvc.perform(post("/api/login/refresh")
                .header(AUTHORIZATION, "Bearer " + accessToken)
                .cookie(refreshTokenCookie)
        );

        //then
        result.andDo(print())
                .andDo(refreshTokenApiDocs())
                .andExpect(status().isOk())
                .andExpect(header().string(AUTHORIZATION, "Bearer " + "NEW_ACCESS_TOKEN"))
                .andExpect(cookie().exists("refreshToken"));
    }

    @DisplayName("로그아웃 시, refreshToken 삭제")
    @Test
    void deleteRefreshToken() throws Exception {
        //given
        final Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        //when
        final ResultActions result = mvc.perform(post("/api/logout")
                .cookie(refreshTokenCookie)
        );

        //then
        result.andDo(print())
                .andDo(logoutApiDocs())
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("refreshToken", 0))
                .andExpect(cookie().value("refreshToken", ""));
    }

    private RestDocumentationResultHandler loginApiDocs() {
        return document("auth/post-login",
                responseHeaders(
                        headerWithName(AUTHORIZATION)
                                .description("JWT accessToken")),
                responseCookies(
                        cookieWithName("refreshToken")
                                .description("JWT refreshToken")
                ),
                queryParameters(
                        parameterWithName("email").description("유저 이메일")
                )
        );
    }

    private RestDocumentationResultHandler refreshTokenApiDocs() {
        return document("auth/post-login-refresh",
                responseHeaders(
                        headerWithName(AUTHORIZATION)
                                .description("JWT accessToken")
                ),
                responseCookies(
                        cookieWithName("refreshToken")
                                .description("JWT refreshToken")
                )
        );
    }

    private RestDocumentationResultHandler logoutApiDocs() {
        return document("auth/post-logout",
                responseCookies(
                        cookieWithName("refreshToken")
                                .description("JWT refreshToken은 빈 값이 된다")
                )
        );
    }
}