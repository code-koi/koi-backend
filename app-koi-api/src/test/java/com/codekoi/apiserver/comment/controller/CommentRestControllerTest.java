package com.codekoi.apiserver.comment.controller;

import com.codekoi.apiserver.comment.dto.HotReviewComment;
import com.codekoi.apiserver.docs.RestDocsCommonDescriptor;
import com.codekoi.apiserver.utils.ControllerTest;
import com.codekoi.koi.KoiType;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.codekoi.apiserver.utils.fixture.UserProfileDtoFixture.PROFILE1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentRestControllerTest extends ControllerTest {


    @Test
    void 핫한_코드리뷰_댓글_목록_조회() throws Exception {
        //given
        final HotReviewComment comment = new HotReviewComment(1L, PROFILE1.toUserProfileDto(), "content",
                KoiType.FISHBOWL, true, 1);

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
                                fieldWithPath("comments[].koiType").type(JsonFieldType.STRING)
                                        .description("코이 타입"),
                                fieldWithPath("comments[].liked").type(JsonFieldType.BOOLEAN)
                                        .description("좋아요 여부").optional(),
                                fieldWithPath("comments[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("좋아요 개수")
                        ).and(RestDocsCommonDescriptor.userProfileDto("comments[]"))
                ));


    }

}