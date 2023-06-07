package codekoi.apiserver.domain.auth.controller;

import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.utils.ControllerTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest extends ControllerTest {

    final String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiaWF0IjoxNjg2MTE5MjYzLCJleHAiOjE2ODYxMTk1NjN9.ZmNReTkQ0pZMXBsdaNDuri5xFQiSYEMYPggt3zj6P-k";
    final String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwiaWF0IjoxNjg2MTE5MjYzLCJleHAiOjE2ODY3MjQwNjN9.3elRxRRR4Moa6U5TLHd2lC0yvN6TLiLu7on37Kadb2o";

    @DisplayName("로그인 시 accessToekn과 refreshToken을 발급한다")
    @Test
    void successLogin() throws Exception {
        //given
        final UserToken userToken = new UserToken(1L);
        given(userQuery.getUserAuth(anyString()))
                .willReturn(userToken);

        given(jwtTokenProvider.createAccessToken(any()))
                .willReturn(accessToken);
        given(jwtTokenProvider.createRefreshToken())
                .willReturn(refreshToken);

        //when, then
        mvc.perform(post("/api/login")
                        .queryParam("email", "sdcodebase@gmail.com")
                ).andDo(print())
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
        final String newAccessToken = "NEW_ACCESS_TOKEN";
        given(jwtTokenProvider.createAccessToken(any()))
                .willReturn(newAccessToken);

        final Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        //when, then
        mvc.perform(post("/api/login/refresh")
                        .header(AUTHORIZATION, "Bearer " + accessToken)
                        .cookie(refreshTokenCookie)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(AUTHORIZATION, "Bearer " + newAccessToken))
                .andExpect(cookie().exists("refreshToken"));
    }

    @DisplayName("로그아웃 시, refreshToken 삭제")
    @Test
    void deleteRefreshToken() throws Exception {
        //given
        final Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        //when, then
        mvc.perform(post("/api/logout")
                        .cookie(refreshTokenCookie)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("refreshToken", 0))
                .andExpect(cookie().value("refreshToken", ""));
    }
}