package com.codekoi.apiserver.like.controller;

import com.codekoi.apiserver.utils.ControllerTest;
import com.codekoi.domain.like.usecase.LikeReviewCommentUseCase;
import com.codekoi.domain.like.usecase.UnlikeReviewCommentUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LikeRestController.class)
class LikeRestControllerTest extends ControllerTest {

    @MockBean
    LikeReviewCommentUseCase likeReviewCommentUseCase;

    @MockBean
    UnlikeReviewCommentUseCase unlikeReviewCommentUseCase;

    @Test
    @DisplayName("코드리뷰 답변에 좋아요")
    void like() throws Exception {

        //when
        final ResultActions result = mvc.perform(
                post("/api/likes/code-comments/{commentId}", 1L)
                        .header(AUTHORIZATION, accessToken)
                        .contentType(APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("likes/post-code-comments-commentId",
                        pathParameters(
                                parameterWithName("commentId")
                                        .description("좋아요할 댓글의 고유 아이디")
                        )
                ));
    }

    @Test
    @DisplayName("코드리뷰 답변에 좋아요 취소")
    void unlike() throws Exception {

        //when
        final ResultActions result = mvc.perform(
                delete("/api/likes/code-comments/{commentId}", 1L)
                        .header(AUTHORIZATION, accessToken)
                        .contentType(APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("likes/delete-code-comments-commentId",
                        pathParameters(
                                parameterWithName("commentId")
                                        .description("좋아요 취소할 댓글의 고유 아이디")
                        )
                ));
    }

}