package codekoi.apiserver.domain.code.review.controller;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.Favorite;
import codekoi.apiserver.domain.code.review.dto.CodeReviewDetailDto;
import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import codekoi.apiserver.domain.skill.doamin.HardSkill;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.utils.ControllerTest;
import codekoi.apiserver.utils.EntityReflectionTestUtil;
import codekoi.apiserver.utils.fixture.HardSkillFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static codekoi.apiserver.utils.fixture.CodeReviewFixture.REVIEW;
import static codekoi.apiserver.utils.fixture.UserFixture.SUNDO;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CodeReviewControllerDocsTest extends ControllerTest {


    @Nested
    @DisplayName("요청한 코드 리뷰 및 즐겨찾기 목록 조회")
    class UserCodeReviewList {
        @Test
        @DisplayName("요청한 코드리뷰 목록 조회")
        void userCodeReviewList() throws Exception {
            //given
            final List<UserCodeReviewDto> dto = setUp();

            given(codeReviewQuery.findRequestedCodeReviews(anyLong(), anyLong()))
                    .willReturn(dto);

            //when
            final ResultActions result = mvc.perform(
                    get("/api/code-reviews")
                            .queryParam("userId", "1")
            );

            //then
            validate(result, "codeReviews/get-userId");
        }

        @Test
        @DisplayName("유저가 즐겨찾기한 코드리뷰 목록 조회")
        void userFavoriteCodeReviews() throws Exception {
            //given
            final List<UserCodeReviewDto> dto = setUp();

            given(codeReviewQuery.findRequestedCodeReviews(anyLong(), anyLong()))
                    .willReturn(dto);

            //when
            final ResultActions result = mvc.perform(
                    get("/api/code-reviews")
                            .queryParam("userId", "1")
            );

            //then
            validate(result, "codeReviews/get-favorite-userId");
        }

        private void validate(ResultActions result, String fileName) throws Exception {
            result
                    .andExpect(status().isOk())
                    .andDo(document(fileName,
                            queryParameters(
                                    parameterWithName("userId")
                                            .description("유저 고유 아이디")
                            ),
                            responseFields(
                                    fieldWithPath("reviews").type(JsonFieldType.ARRAY)
                                            .description("리뷰 목록 배열"),

                                    fieldWithPath("reviews[].user.profileImageUrl").type(JsonFieldType.STRING)
                                            .description("프로필 이미지").optional(),
                                    fieldWithPath("reviews[].user.nickname").type(JsonFieldType.STRING)
                                            .description("닉네임"),
                                    fieldWithPath("reviews[].user.id").type(JsonFieldType.NUMBER)
                                            .description("유저 고유 아이디"),

                                    fieldWithPath("reviews[].createdAt").type(JsonFieldType.STRING)
                                            .description("코드리뷰를 요청한 날"),
                                    fieldWithPath("reviews[].title").type(JsonFieldType.STRING)
                                            .description("제목"),
                                    fieldWithPath("reviews[].status").type(JsonFieldType.STRING)
                                            .description("리뷰 상태 PENDING(진행중), RESOLVED(해결 완료)"),
                                    fieldWithPath("reviews[].skills").type(JsonFieldType.ARRAY)
                                            .description("스킬 목록"),
                                    fieldWithPath("reviews[].reviewId").type(JsonFieldType.NUMBER)
                                            .description("요청한 코드리뷰 고유 아이디"),
                                    fieldWithPath("reviews[].isFavorite").type(JsonFieldType.BOOLEAN)
                                            .description("세션 유저가 해당 리뷰건에 대해 즐겨찾기 여부." +
                                                    "세션유저가 즐겨찾기 한 경우 true, 세션유저가 자신의 프로필이 아니거나, 즐겨찾기를 하지 않은 경우 false")
                            )
                    ));
        }

        private List<UserCodeReviewDto> setUp() {
            final User user = SUNDO.toUser(1L);

            final CodeReview codeReview = REVIEW.toCodeReview(1L, user);
            EntityReflectionTestUtil.setCreatedAt(codeReview, LocalDateTime.now());

            final HardSkill hardSkill = HardSkillFixture.JPA.toHardSkill();
            codeReview.addCodeReviewSkill(hardSkill);

            final Favorite favorite = Favorite.of(codeReview, user);

            return UserCodeReviewDto.listOf(List.of(codeReview), List.of(favorite), true);
        }
    }

    @Test
    @DisplayName("코드리뷰 상세의 리뷰 관련 정보 조회")
    void reviewDetail() throws Exception {
        //given
        final User user = SUNDO.toUser(1L);

        final CodeReview codeReview = REVIEW.toCodeReview(1L, user);
        EntityReflectionTestUtil.setCreatedAt(codeReview, LocalDateTime.now());

        final HardSkill hardSkill = HardSkillFixture.JPA.toHardSkill();
        codeReview.addCodeReviewSkill(hardSkill);

        final CodeReviewDetailDto dto = CodeReviewDetailDto.of(codeReview, true, true);
        given(codeReviewQuery.findCodeReviewDetail(anyLong(), anyLong()))
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
}
