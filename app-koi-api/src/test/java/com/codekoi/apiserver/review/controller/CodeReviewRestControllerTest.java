package com.codekoi.apiserver.review.controller;

import com.codekoi.apiserver.comment.dto.CommentReviewDetailDto;
import com.codekoi.apiserver.comment.service.ReviewCommentQueryService;
import com.codekoi.apiserver.review.dto.CodeReviewDetailDto;
import com.codekoi.apiserver.review.dto.HotCodeReview;
import com.codekoi.apiserver.review.service.CodeReviewQueryService;
import com.codekoi.apiserver.utils.ControllerTest;
import com.codekoi.apiserver.utils.EntityReflectionTestUtil;
import com.codekoi.domain.comment.entity.ReviewComment;
import com.codekoi.domain.review.entity.CodeReview;
import com.codekoi.domain.skill.skill.entity.Skill;
import com.codekoi.domain.user.entity.User;
import com.codekoi.fixture.CodeReviewFixture;
import com.codekoi.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.codekoi.fixture.CodeReviewFixture.REVIEW;
import static com.codekoi.fixture.ReviewCommentFixture.REVIEW_COMMENT;
import static com.codekoi.fixture.SkillFixture.JPA;
import static com.codekoi.fixture.UserFixture.SUNDO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CodeReviewRestController.class)
class CodeReviewRestControllerTest extends ControllerTest {

    @MockBean
    CodeReviewQueryService codeReviewQueryService;

    @MockBean
    ReviewCommentQueryService reviewCommentQueryService;


    @Test
    @DisplayName("코드리뷰 상세의 리뷰 관련 정보 조회")
    void reviewDetail() throws Exception {
        //given
        final User user = SUNDO.toUser(1L);

        final CodeReview codeReview = REVIEW.toCodeReview(1L, user);
        EntityReflectionTestUtil.setCreatedAt(codeReview, LocalDateTime.now());

        final Skill hardSkill = JPA.toHardSkill();
        codeReview.addCodeReviewSkill(hardSkill);

        final CodeReviewDetailDto dto = CodeReviewDetailDto.of(codeReview, true, true);
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

                                fieldWithPath("review.user.profileImageUrl").type(JsonFieldType.STRING)
                                        .description("프로필 이미지").optional(),
                                fieldWithPath("review.user.nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("review.user.id").type(JsonFieldType.NUMBER)
                                        .description("유저 고유 아이디"),

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
                        )
                ));
    }

    @Test
    @DisplayName("코드리뷰 요청에 대한 댓글 목록 조회")
    void commentsOnReview() throws Exception {
        //given
        final User user1 = UserFixture.HONG.toUser(1L);
        final CodeReview codeReview = CodeReviewFixture.REVIEW.toCodeReview(1L, user1);

        final User user2 = SUNDO.toUser(2L);
        final ReviewComment reviewComment = REVIEW_COMMENT.toCodeReviewComment(3L, user2, codeReview);
        EntityReflectionTestUtil.setCreatedAt(reviewComment, LocalDateTime.now());

        final List<CommentReviewDetailDto> dto = CommentReviewDetailDto.listOf(List.of(reviewComment), List.of(), 10L, List.of());

        given(reviewCommentQueryService.getCommentsOnReview(any(), any()))
                .willReturn(dto);

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

                                fieldWithPath("comments[].user").type(JsonFieldType.OBJECT)
                                        .description("댓글을 남긴 유저 정보"),
                                fieldWithPath("comments[].user.profileImageUrl").type(JsonFieldType.STRING)
                                        .description("프로필 이미지").optional(),
                                fieldWithPath("comments[].user.nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("comments[].user.id").type(JsonFieldType.NUMBER)
                                        .description("유저 고유 아이디"),


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
                        )
                ));
    }

    @Test
    @DisplayName("인기있는 코드리뷰 요청 목록 조회")
    void hotCodeReview() throws Exception {
        //given
        final User user1 = UserFixture.HONG.toUser(1L);
        final CodeReview codeReview = CodeReviewFixture.REVIEW.toCodeReview(2L, user1);

        EntityReflectionTestUtil.setCreatedAt(codeReview, LocalDateTime.now());

        final Skill hardSkill = JPA.toHardSkill();
        codeReview.addCodeReviewSkill(hardSkill);

        final List<HotCodeReview> dto = HotCodeReview.listFrom(List.of(codeReview));

        given(codeReviewQueryService.getHotReviews())
                .willReturn(dto);

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
                                        .description("인기있는 코드리뷰 목록"),

                                fieldWithPath("reviews[].id").type(JsonFieldType.NUMBER)
                                        .description("리뷰 요청 아이디"),
                                fieldWithPath("reviews[].title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("reviews[].status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("reviews[].createdAt").type(JsonFieldType.STRING)
                                        .description("생성일"),

                                fieldWithPath("reviews[].user").type(JsonFieldType.OBJECT)
                                        .description("유저 정보"),
                                fieldWithPath("reviews[].user.profileImageUrl").type(JsonFieldType.STRING)
                                        .description("프로필 이미지").optional(),
                                fieldWithPath("reviews[].user.nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("reviews[].user.id").type(JsonFieldType.NUMBER)
                                        .description("유저 고유 아이디"),

                                fieldWithPath("reviews[].skills").type(JsonFieldType.ARRAY)
                                        .description("스킬 목록")
                        )
                ));
    }
}