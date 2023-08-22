package com.codekoi.apiserver.auth.controller;

import com.codekoi.apiserver.auth.service.AuthService;
import com.codekoi.apiserver.user.service.UserQueryService;
import com.codekoi.coreweb.jwt.AuthInfo;
import com.codekoi.coreweb.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = AuthRestController.class)
@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthRestControllerTest {

    private String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiaWF0IjoxNjgwMDA5MjYzLCJleHAiOjE2ODYxODE5NTYzfQ.P5gzo1eNYtoog5ZtNpuqpWLqQDu2zRH1Rcdt-u_QUtQ";
    private String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwiaWF0IjoxNjg2MTE5MjYzLCJleHAiOjE2ODY3MjQwNjN9.3elRxRRR4Moa6U5TLHd2lC0yvN6TLiLu7on37Kadb2o";

    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private UserQueryService userQueryService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthService authService;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
        this.mvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .addFilter(new CharacterEncodingFilter("UTF-8", true))
                        .apply(documentationConfiguration(provider)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint()))
                        .alwaysDo(print())
                        .build();
    }

    @Test
    void 로그인_시_accessToken과_refreshToken을_발급한다() throws Exception {
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

    @Test
    void refreshToken을_이용해서_accessToken을_발급한다() throws Exception {
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

    @Test
    void 로그아웃_시__refreshToken_삭제() throws Exception {
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