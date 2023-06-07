package codekoi.apiserver.docs;

import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.utils.ControllerTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerDocsTest extends ControllerTest {

    @DisplayName("로그인")
    @Test
    void login() throws Exception {
        given(userQuery.getUserAuth(any()))
                .willReturn(new UserToken(1L));

        given(jwtTokenProvider.createAccessToken(any()))
                .willReturn("accessToken");

        given(jwtTokenProvider.createRefreshToken())
                .willReturn("refreshToken");

        final ResultActions result = mvc.perform(
                post("/api/login")
                        .queryParam("email", "sdcodebase@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isOk())
                .andDo(document("auth/post-login",
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
                ));
    }

    @DisplayName("accessToken 새로 받기")
    @Test
    void getNewAccessToken() throws Exception {

        given(jwtTokenProvider.createAccessToken(any()))
                .willReturn("NEW_ACCESS_TOKEN");

        final ResultActions result = mvc.perform(
                post("/api/login/refresh")
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isOk())
                .andDo(document("auth/post-login-refresh",
                        responseHeaders(
                                headerWithName(AUTHORIZATION)
                                        .description("JWT accessToken")
                        ),
                        responseCookies(
                                cookieWithName("refreshToken")
                                        .description("JWT refreshToken")
                        )
                ));
    }

    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {
        final ResultActions result = mvc.perform(
                post("/api/logout")
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isOk())
                .andDo(document("auth/post-logout",
                        responseCookies(
                                cookieWithName("refreshToken")
                                        .description("JWT refreshToken은 빈 값이 된다")
                        )
                ));

    }
}
