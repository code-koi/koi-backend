package codekoi.apiserver.domain.user.controller;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.comment.dto.UserCodeCommentDto;
import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.Favorite;
import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import codekoi.apiserver.domain.code.review.dto.UserSkillStatistics;
import codekoi.apiserver.domain.koi.domain.KoiType;
import codekoi.apiserver.domain.skill.doamin.Skill;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.dto.UserDetail;
import codekoi.apiserver.utils.ControllerTest;
import codekoi.apiserver.utils.EntityReflectionTestUtil;
import codekoi.apiserver.utils.fixture.CodeReviewFixture;
import codekoi.apiserver.utils.fixture.SkillFixture;
import codekoi.apiserver.utils.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static codekoi.apiserver.utils.fixture.CodeReviewCommentFixture.REVIEW_COMMENT;
import static codekoi.apiserver.utils.fixture.CodeReviewFixture.REVIEW;
import static codekoi.apiserver.utils.fixture.UserFixture.SUNDO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerDocsTest extends ControllerTest {

    @Test
    @DisplayName("유저 상세조회")
    void userDetail() throws Exception {
        final UserDetail.Skill skill = new UserDetail.Skill(1L, "JPA");
        final UserDetail dto = new UserDetail(true, SUNDO.nickname, SUNDO.email, SUNDO.profileImageUrl, SUNDO.years, SUNDO.introduce,
                new UserDetail.Activity(1), List.of(skill));

        given(userQuery.gerUserDetail(any(), anyLong()))
                .willReturn(dto);

        final ResultActions result = mvc.perform(get("/api/users/{userId}", 1L));

        result
                .andExpect(status().isOk())
                .andDo(document("users/get",
                        pathParameters(
                                parameterWithName("userId")
                                        .description("유저 고유아이디")
                        ),
                        responseFields(
                                fieldWithPath("me").type(JsonFieldType.BOOLEAN)
                                        .description("내 자신의 프로필인지 여부"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("years").type(JsonFieldType.NUMBER)
                                        .description("연차 정보"),
                                fieldWithPath("introduce").type(JsonFieldType.STRING)
                                        .description("자기소개").optional(),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING)
                                        .description("프로필 이미지 경로").optional(),
                                fieldWithPath("activity").type(JsonFieldType.OBJECT)
                                        .description("활동 관련"),
                                fieldWithPath("activity.codeReview").type(JsonFieldType.NUMBER)
                                        .description("코드리뷰 활동 건수"),
                                fieldWithPath("skills").type(JsonFieldType.ARRAY)
                                        .description("기술 스택"),
                                fieldWithPath("skills[].id").type(JsonFieldType.NUMBER)
                                        .description("기술 스택 아이디"),
                                fieldWithPath("skills[].name").type(JsonFieldType.STRING)
                                        .description("기술 스택 이름")
                        )
                ));
    }

    @Test
    @DisplayName("코드리뷰 응답 댓글 목록 조회")
    void codeReviewCommentList() throws Exception {
        //given
        final User user1 = UserFixture.HONG.toUser(1L);
        final CodeReview codeReview = CodeReviewFixture.REVIEW.toCodeReview(2L, user1);

        final User user2 = SUNDO.toUser(2L);
        final CodeReviewComment reviewComment = REVIEW_COMMENT.toCodeReviewComment(3L, user2, codeReview);
        EntityReflectionTestUtil.setCreatedAt(reviewComment, LocalDateTime.now());

        final UserCodeCommentDto dto = UserCodeCommentDto.of(user2, reviewComment, KoiType.FISHBOWL, 2L);
        given(codeCommentQuery.getUserComments(anyLong()))
                .willReturn(List.of(dto));

        //when
        final ResultActions result = mvc.perform(
                get("/api/users/{userId}/comments", 1L)
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("users/get-userId-comments",
                        pathParameters(
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
                                fieldWithPath("comments[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("좋아요 개수"),
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
                    get("/api/users/{userId}/reviews", 1L)
            );

            //then
            validate(result, "users/get-userId-reviews");
        }

        @Test
        @DisplayName("유저가 즐겨찾기한 코드리뷰 목록 조회")
        void userFavoriteCodeReviews() throws Exception {
            //given
            final List<UserCodeReviewDto> dto = setUp();

            given(codeReviewQuery.findFavoriteCodeReviews(anyLong(), anyLong()))
                    .willReturn(dto);

            //when
            final ResultActions result = mvc.perform(
                    get("/api/users/{userId}/favorite", 1L)
            );

            //then
            validate(result, "users/get-userId-favorite");
        }

        private void validate(ResultActions result, String fileName) throws Exception {
            result
                    .andExpect(status().isOk())
                    .andDo(document(fileName,
                            pathParameters(
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

            final Skill hardSkill = SkillFixture.JPA.toHardSkill();
            codeReview.addCodeReviewSkill(hardSkill);

            final Favorite favorite = Favorite.of(codeReview, user);

            return UserCodeReviewDto.listOf(List.of(codeReview), List.of(favorite), true);
        }
    }

    @Test
    @DisplayName("유저의 스킬 통계 정보 조회")
    void userSkillStatistics() throws Exception {
        //given
        final Skill skill = SkillFixture.JPA.toHardSkill(1L);
        final UserSkillStatistics statistics = UserSkillStatistics.of(skill, 10);

        given(codeReviewQuery.findUserSkillStatistics(anyLong()))
                .willReturn(List.of(statistics));

        //when
        final ResultActions result = mvc.perform(get("/api/users/{userId}/skills/statistics", 1L));

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("users/get-userId-skills-statistics",
                        responseFields(
                                fieldWithPath("skills").type(JsonFieldType.ARRAY)
                                        .description("유저의 스킬 배열. 많이 사용한 순으로 내림차순 정렬"),
                                fieldWithPath("skills[].id").type(JsonFieldType.NUMBER)
                                        .description("스킬 아이디"),
                                fieldWithPath("skills[].name").type(JsonFieldType.STRING)
                                        .description("스킬 이름"),
                                fieldWithPath("skills[].count").type(JsonFieldType.NUMBER)
                                        .description("해당 스킬으로 코드리뷰 요청 및 응답한 횟수")
                        )
                ));
    }
}
