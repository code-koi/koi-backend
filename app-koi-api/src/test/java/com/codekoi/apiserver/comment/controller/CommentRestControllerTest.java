package com.codekoi.apiserver.comment.controller;

import com.codekoi.apiserver.comment.dto.HotReviewComment;
import com.codekoi.apiserver.comment.service.ReviewCommentQueryService;
import com.codekoi.apiserver.user.dto.UserProfileDto;
import com.codekoi.apiserver.utils.ControllerTest;
import com.codekoi.domain.koi.KoiType;
import com.codekoi.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentRestController.class)
class CommentRestControllerTest extends ControllerTest {

    @MockBean
    private ReviewCommentQueryService reviewCommentQueryService;


    @Test
    @DisplayName("핫한 코드리뷰 댓글 목록 조회")
    void hotComment() throws Exception {
        //given
        final HotReviewComment comment = new HotReviewComment(1L, UserProfileDto.from(UserFixture.HONG.toUser(1L)),
                "content", KoiType.FISHBOWL, true, 1);

        given(reviewCommentQueryService.getHotComments(any()))
                .willReturn(List.of(comment));

        //then
        final ResultActions result = mvc.perform(
                get("/api/comments/hot")
        );

        result
                .andExpect(status().isOk())
                .andDo(document("comments/get-hots",

                        responseFields(
                                fieldWithPath("comments").type(JsonFieldType.ARRAY)
                                        .description("핫한 코드리뷰 응답 목록"),

                                fieldWithPath("comments[].id").type(JsonFieldType.NUMBER)
                                        .description("리뷰 아이디"),
                                fieldWithPath("comments[].content").type(JsonFieldType.STRING)
                                        .description("리뷰 댓글 내용"),

                                fieldWithPath("comments[].user").type(JsonFieldType.OBJECT)
                                        .description("댓글을 남긴 유저 정보"),
                                fieldWithPath("comments[].user.profileImageUrl").type(JsonFieldType.STRING)
                                        .description("프로필 이미지").optional(),
                                fieldWithPath("comments[].user.nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("comments[].user.id").type(JsonFieldType.NUMBER)
                                        .description("유저 고유 아이디"),

                                fieldWithPath("comments[].koiType").type(JsonFieldType.STRING)
                                        .description("코이 타입"),
                                fieldWithPath("comments[].liked").type(JsonFieldType.BOOLEAN)
                                        .description("좋아요 여부").optional(),
                                fieldWithPath("comments[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("좋아요 개수")
                        )
                ));


    }

}