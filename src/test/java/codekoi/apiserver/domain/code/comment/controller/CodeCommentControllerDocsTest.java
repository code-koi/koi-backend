package codekoi.apiserver.domain.code.comment.controller;


import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.dto.UserCodeCommentDto;
import codekoi.apiserver.domain.koi.domain.KoiType;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.utils.ControllerTest;
import codekoi.apiserver.utils.EntityReflectionTestUtil;
import codekoi.apiserver.utils.fixture.CodeReviewFixture;
import codekoi.apiserver.utils.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static codekoi.apiserver.utils.fixture.CodeReviewCommentFixture.REVIEW_COMMENT;
import static codekoi.apiserver.utils.fixture.UserFixture.SUNDO;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CodeCommentControllerDocsTest extends ControllerTest {

    @Test
    @DisplayName("코드리뷰 응답 댓글 목록 조회")
    void codeReviewCommentList() throws Exception {
        //given
        final User user1 = UserFixture.HONG.toUser(1L);
        final CodeReview codeReview = CodeReviewFixture.REVIEW.toCodeReview(2L, user1);

        final User user2 = SUNDO.toUser(2L);
        final CodeReviewComment reviewComment = REVIEW_COMMENT.toCodeReviewComment(3L, user2, codeReview);
        EntityReflectionTestUtil.setCreatedAt(reviewComment, LocalDateTime.now());

        final UserCodeCommentDto dto = UserCodeCommentDto.of(user2, reviewComment, KoiType.FISHBOWL);
        given(codeCommentQuery.getUserComments(anyLong()))
                .willReturn(List.of(dto));

        //when
        final ResultActions result = mvc.perform(
                get("/api/code-comments")
                        .queryParam("userId", "1")
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("codeComments/get-userId",
                        queryParameters(
                                parameterWithName("userId")
                                        .description("유저 고유아이디")
                        ),
                        responseFields(
                                fieldWithPath("comments").type(JsonFieldType.ARRAY)
                                        .description("유저가 남긴 코드리뷰 댓글 목록"),

                                fieldWithPath("comments[].user").type(JsonFieldType.OBJECT)
                                        .description("댓글을 남긴 유저 정보 (모두 같은 값)"),
                                fieldWithPath("comments[].user.profileImageUrl").type(JsonFieldType.STRING)
                                        .description("프로필 이미지").optional(),
                                fieldWithPath("comments[].user.nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("comments[].user.id").type(JsonFieldType.NUMBER)
                                        .description("유저 고유 아이디"),

                                fieldWithPath("comments[].createdAt").type(JsonFieldType.STRING)
                                        .description("댓글 남긴 시각"),
                                fieldWithPath("comments[].reviewId").type(JsonFieldType.NUMBER)
                                        .description("코드리뷰 요청의 고유 아이디"),
                                fieldWithPath("comments[].content").type(JsonFieldType.STRING)
                                        .description("댓글 내용"),
                                fieldWithPath("comments[].koiType").type(JsonFieldType.STRING)
                                        .description("코이 값 /" +
                                                " FISHBOWL(어항), STREAM(시냇물), RIVER(강물), SEA(바다) /" +
                                                "코이 지급을 하지 않으면 값이 없다.").optional()
                        )
                ));
    }

}