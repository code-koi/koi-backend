package com.codekoi.apiserver.review.controller;

import com.codekoi.apiserver.comment.dto.CommentReviewDetailDto;
import com.codekoi.apiserver.docs.RestDocsCommonDescriptor;
import com.codekoi.apiserver.review.controller.request.CreateCodeReviewRequest;
import com.codekoi.apiserver.review.controller.request.UpdateCodeReveiwRequest;
import com.codekoi.apiserver.review.dto.BasicCodeReview;
import com.codekoi.apiserver.review.dto.CodeReviewDetailDto;
import com.codekoi.apiserver.utils.ControllerTest;
import com.codekoi.domain.koi.KoiType;
import com.codekoi.domain.review.CodeReviewStatus;
import com.codekoi.pagination.NoOffSetPagination;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.codekoi.apiserver.utils.fixture.UserProfileDtoFixture.PROFILE1;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CodeReviewRestControllerTest extends ControllerTest {

    @Test
    void 코드리뷰_목록_조회() throws Exception {
        //given
        final BasicCodeReview dto = new BasicCodeReview(1L, "제목", CodeReviewStatus.PENDING,
                LocalDateTime.now(), PROFILE1.toUserProfileDto(), List.of("Java"));
        final NoOffSetPagination<BasicCodeReview, Long> response = new NoOffSetPagination<>(List.of(dto), true, 12L);
        given(codeReviewQueryService.getReviewList(any()))
                .willReturn(response);

        //when
        final ResultActions result = mvc.perform(
                get("/api/code-reviews")
                        .param("status", CodeReviewStatus.PENDING.toString())
                        .param("skillIds", "1", "2")
                        .param("lastId", "11")
                        .param("title", "transaction")
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("codeReviews/get",
                                queryParameters(
                                        parameterWithName("status")
                                                .description("코드리뷰 상태, PENDING, RESOLVED. 조건없으면 안보내기").optional(),
                                        parameterWithName("skillIds")
                                                .description("기술 태그").optional(),
                                        parameterWithName("lastId")
                                                .description("현재 페이지의 마지막 reviewId. 처음에는 없고 서버가 주는 값 그대로 전달").optional(),
                                        parameterWithName("title")
                                                .description("제목 키워드").optional()
                                ),

                                responseFields(
                                        fieldWithPath("list").type(JsonFieldType.ARRAY)
                                                .description("코드리뷰 목록"),

                                        fieldWithPath("list[].id").type(JsonFieldType.NUMBER)
                                                .description("리뷰 요청 아이디"),
                                        fieldWithPath("list[].title").type(JsonFieldType.STRING)
                                                .description("제목"),
                                        fieldWithPath("list[].status").type(JsonFieldType.STRING)
                                                .description("상태"),
                                        fieldWithPath("list[].createdAt").type(JsonFieldType.STRING)
                                                .description("생성일"),

                                        fieldWithPath("list[].skills").type(JsonFieldType.ARRAY)
                                                .description("스킬 목록"),

                                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                                .description("다음 페이지 있는지 여부"),
                                        fieldWithPath("lastId").type(JsonFieldType.NUMBER)
                                                .description("현 페이지의 마지막 리뷰 ID. 첫 요청시 보내지 않음")
                                ).and(RestDocsCommonDescriptor.userProfileDto("list[]"))
                        )
                );
    }

    @Test
    void 코드리뷰_상세의_리뷰_관련_정보_조회() throws Exception {
        //given
        final CodeReviewDetailDto dto = new CodeReviewDetailDto(PROFILE1.toUserProfileDto(), LocalDateTime.now(), "title", List.of("JPA"),
                CodeReviewStatus.PENDING, true, true);

        given(codeReviewQueryService.findCodeReviewDetail(any(), any()))
                .willReturn(dto);

        //when
        final ResultActions result = mvc.perform(
                get("/api/code-reviews/{reviewId}", 1L)
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("codeReviews/get-reviewId",
                        pathParameters(
                                parameterWithName("reviewId")
                                        .description("코드리뷰 고유 아이디")
                        ),
                        responseFields(
                                fieldWithPath("review").type(JsonFieldType.OBJECT)
                                        .description("리뷰 정보"),

                                fieldWithPath("review.createdAt").type(JsonFieldType.STRING)
                                        .description("코드리뷰를 요청한 날"),
                                fieldWithPath("review.title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("review.status").type(JsonFieldType.STRING)
                                        .description("리뷰 상태 PENDING(진행중), RESOLVED(해결 완료)"),
                                fieldWithPath("review.skills").type(JsonFieldType.ARRAY)
                                        .description("스킬 목록"),
                                fieldWithPath("review.isFavorite").type(JsonFieldType.BOOLEAN)
                                        .description("세션 유저가 해당 리뷰건에 대해 즐겨찾기 여부." +
                                                "세션유저가 즐겨찾기 한 경우 true, 세션유저가 자신의 프로필이 아니거나, 즐겨찾기를 하지 않은 경우 false"),
                                fieldWithPath("review.me").type(JsonFieldType.BOOLEAN)
                                        .description("로그인된 유저와 리뷰요청을 남긴 유저가 같으면 true")
                        ).and(RestDocsCommonDescriptor.userProfileDto("review"))
                ));
    }

    @Test
    void 코드리뷰_요청에_대한_댓글_목록_조회() throws Exception {
        //given
        final CommentReviewDetailDto dto = new CommentReviewDetailDto(PROFILE1.toUserProfileDto(), 2L, LocalDateTime.now(), "코드리뷰 요청합니다",
                KoiType.FISHBOWL, true, 1L, true);

        given(reviewCommentQueryService.getCommentsOnReview(any(), any()))
                .willReturn(List.of(dto));

        //when
        final ResultActions result = mvc.perform(
                get("/api/code-reviews/{reviewId}/comments", 1L)
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("codeReviews/get-reviewId-comments",
                        pathParameters(
                                parameterWithName("reviewId")
                                        .description("코드리뷰 고유아이디")
                        ),
                        responseFields(
                                fieldWithPath("comments").type(JsonFieldType.ARRAY)
                                        .description("코드리뷰에 대해 남긴 댓글 목록"),

                                fieldWithPath("comments[].id").type(JsonFieldType.NUMBER)
                                        .description("댓글 고유아이디"),
                                fieldWithPath("comments[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("좋아요 개수"),
                                fieldWithPath("comments[].createdAt").type(JsonFieldType.STRING)
                                        .description("댓글 남긴 시각"),
                                fieldWithPath("comments[].content").type(JsonFieldType.STRING)
                                        .description("댓글 내용"),
                                fieldWithPath("comments[].koiType").type(JsonFieldType.STRING)
                                        .description("코이 값").optional(),
                                fieldWithPath("comments[].me").type(JsonFieldType.BOOLEAN)
                                        .description("현재 로그인한 유저가 작성한 댓글인지 여부"),
                                fieldWithPath("comments[].liked").type(JsonFieldType.BOOLEAN)
                                        .description("현재 로그인한 유저가 해당 댓글에 좋아요 했는 지 여부")
                        ).and(RestDocsCommonDescriptor.userProfileDto("comments[]"))
                ));
    }

    @Test
    void 인기있는_코드리뷰_요청_목록_조회() throws Exception {
        //given
        final BasicCodeReview dto = new BasicCodeReview(1L, "제목", CodeReviewStatus.PENDING, LocalDateTime.now(),
                PROFILE1.toUserProfileDto(), List.of("JPA"));

        given(codeReviewQueryService.getHotReviews())
                .willReturn(List.of(dto));

        //when
        final ResultActions result = mvc.perform(
                get("/api/code-reviews/hot")
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("codeReviews/get-hot",
                        responseFields(
                                fieldWithPath("reviews").type(JsonFieldType.ARRAY)
                                        .description("코드리뷰 목록"),

                                fieldWithPath("reviews[].id").type(JsonFieldType.NUMBER)
                                        .description("리뷰 요청 아이디"),
                                fieldWithPath("reviews[].title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("reviews[].status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("reviews[].createdAt").type(JsonFieldType.STRING)
                                        .description("생성일"),

                                fieldWithPath("reviews[].skills").type(JsonFieldType.ARRAY)
                                        .description("스킬 목록")
                        ).and(RestDocsCommonDescriptor.userProfileDto("reviews[]"))
                ));
    }

    @Test
    void 코드리뷰_생성() throws Exception {
        //given
        final List<Long> SKILL_IDS = List.of(1L);
        final CreateCodeReviewRequest request = new CreateCodeReviewRequest("제목", "내용", SKILL_IDS);

        final long CREATED_CODE_REVIEW_ID = 2L;
        given(codeReviewService.create(anyLong(), anyString(), anyString(), anyList()))
                .willReturn(CREATED_CODE_REVIEW_ID);

        //when
        final ResultActions result = mvc.perform(
                post("/api/code-reviews")
                        .header(AUTHORIZATION, accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("codeReviews/post",
                                requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING)
                                                .description("코드리뷰 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("코드리뷰 내용"),
                                        fieldWithPath("skillIds").type(JsonFieldType.ARRAY)
                                                .description("스킬 아이디 목록")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                                .description("생성된 코드리뷰 아이디")
                                )
                        )
                );
    }

    @Test
    void 코드리뷰_수정() throws Exception {
        //given
        final String UPDATE_TITLE = "변경할 제목";
        final String UPDATE_CONTENT = "변경할 내용";
        final List<Long> UPDATE_SKILL_IDS = List.of(1L, 2L);
        UpdateCodeReveiwRequest request = new UpdateCodeReveiwRequest(UPDATE_TITLE, UPDATE_CONTENT, UPDATE_SKILL_IDS);

        //when
        final ResultActions result = mvc.perform(
                put("/api/code-reviews/{reviewId}", 1L)
                        .header(AUTHORIZATION, accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("codeReviews/put-reviewId",
                                pathParameters(
                                        parameterWithName("reviewId")
                                                .description("코드리뷰 고유아이디")
                                ),
                                requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING)
                                                .description("코드리뷰 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("코드리뷰 내용"),
                                        fieldWithPath("skillIds").type(JsonFieldType.ARRAY)
                                                .description("스킬 아이디 목록")
                                )
                        )
                );
    }

    @Test
    void 코드리뷰_삭제() throws Exception {
        //given

        //when
        final ResultActions result = mvc.perform(
                delete("/api/code-reviews/{reviewId}", 1L)
                        .header(AUTHORIZATION, accessToken)
                        .contentType(APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("codeReviews/delete-reviewId",
                                pathParameters(
                                        parameterWithName("reviewId")
                                                .description("코드리뷰 고유아이디")
                                )
                        )
                );
    }
}