package codekoi.apiserver.domain.code.comment.controller;


import codekoi.apiserver.utils.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CodeCommentControllerDocsTest extends ControllerTest {

    @Test
    @DisplayName("코드리뷰 답변에 좋아요")
    void like() throws Exception {

        //when
        final ResultActions result = mvc.perform(
                post("/api/code-comments/{commentId}/like", 1L)
                        .header(AUTHORIZATION, accessToken)
                        .contentType(APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("codeComments/post-commentId-like",
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
                post("/api/code-comments/{commentId}/unlike", 1L)
                        .header(AUTHORIZATION, accessToken)
                        .contentType(APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("codeComments/post-commentId-unlike",
                        pathParameters(
                                parameterWithName("commentId")
                                        .description("좋아요 취소할 댓글의 고유 아이디")
                        )
                ));
    }
}