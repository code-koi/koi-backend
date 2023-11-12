package com.codekoi.apiserver.user.controller;

import com.codekoi.apiserver.comment.dto.UserCodeCommentDto;
import com.codekoi.apiserver.review.dto.UserActivityHistory;
import com.codekoi.apiserver.review.dto.UserCodeReviewDto;
import com.codekoi.apiserver.review.vo.Activity;
import com.codekoi.apiserver.review.vo.ActivityHistories;
import com.codekoi.apiserver.skill.review.dto.UserSkillStatistics;
import com.codekoi.apiserver.user.dto.UserDetail;
import com.codekoi.apiserver.utils.ControllerTest;
import com.codekoi.domain.koi.KoiType;
import com.codekoi.domain.review.CodeReviewStatus;
import com.codekoi.domain.skill.skill.Skill;
import com.codekoi.fixture.SkillFixture;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.codekoi.apiserver.utils.fixture.UserProfileDtoFixture.PROFILE1;
import static com.codekoi.fixture.UserFixture.SUNDO;
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

class UserRestControllerTest extends ControllerTest {

    @Test
    void 유저_상세조회() throws Exception {
        //given
        final UserDetail.Skill skill = new UserDetail.Skill(1L, "JPA");
        final UserDetail dto = new UserDetail(true, SUNDO.nickname, SUNDO.email, SUNDO.profileImageUrl, SUNDO.years, SUNDO.introduce,
                new UserDetail.Activity(1), List.of(skill));

        given(userQueryService.getUserDetail(any(), anyLong()))
                .willReturn(dto);
        //when
        final ResultActions result = mvc.perform(get("/api/users/{userId}", 1L));

        //then
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
    void 코드리뷰_응답_댓글_목록_조회() throws Exception {
        //given
        final UserCodeCommentDto dto = new UserCodeCommentDto(PROFILE1.toUserProfileDto(), 2L, LocalDateTime.now(), "글 내용",
                KoiType.FISHBOWL, 2L);
        given(reviewCommentQueryService.getUserComments(anyLong()))
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
    class 요청한_코드_리뷰_및_즐겨찾기_목록_조회 {
        @Test
        void 요청한_코드리뷰_목록_조회() throws Exception {
            //given
            final List<UserCodeReviewDto> dto = setUp();

            given(codeReviewQueryService.findRequestedCodeReviews(anyLong(), anyLong()))
                    .willReturn(dto);

            //when
            final ResultActions result = mvc.perform(
                    get("/api/users/{userId}/reviews", 1L)
            );

            //then
            validate(result, "users/get-userId-reviews");
        }

        @Test
        void 유저가_즐겨찾기한_코드리뷰_목록_조회() throws Exception {
            //given
            final List<UserCodeReviewDto> dto = setUp();

            given(codeReviewQueryService.findFavoriteCodeReviews(anyLong(), anyLong()))
                    .willReturn(dto);

            //when
            final ResultActions result = mvc.perform(
                    get("/api/users/{userId}/favorite/reviews", 1L)
            );

            //then
            validate(result, "users/get-userId-favorite-reviews");
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
            final UserCodeReviewDto dto = new UserCodeReviewDto(PROFILE1.toUserProfileDto(), LocalDateTime.now(), "글 내용",
                    List.of("JPA"), CodeReviewStatus.PENDING, 2L, true);

            return List.of(dto);
        }
    }

    @Test
    void 유저의_스킬_통계_정보_조회() throws Exception {
        //given
        final Skill skill = SkillFixture.JPA.toSkill(1L);
        final UserSkillStatistics statistics = UserSkillStatistics.of(skill, 10);

        given(codeReviewSkillQueryService.findUserSkillStatistics(anyLong()))
                .willReturn(List.of(statistics));

        //when
        final ResultActions result = mvc.perform(get("/api/users/{userId}/skills/statistics", 1L));

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("users/get-userId-skills-statistics",
                        pathParameters(
                                parameterWithName("userId")
                                        .description("유저 고유 아이디")
                        ),

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

    @Test
    void 유저의_활동_로그_조회() throws Exception {
        //given
        final Activity activity = new Activity(Activity.Type.LIKE, 1L, "이 코드 여기서 이상합니다.", LocalDateTime.now());
        final List<UserActivityHistory> dto = UserActivityHistory.listFrom(List.of(activity));

        given(codeReviewQueryService.findUserHistory(anyLong()))
                .willReturn(dto);

        //when
        final ResultActions result = mvc.perform(get("/api/users/{userId}/logs", 1L));

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("users/get-userId-logs", pathParameters(
                                parameterWithName("userId")
                                        .description("유저 고유 아이디")
                        ),
                        responseFields(
                                fieldWithPath("logs").type(JsonFieldType.ARRAY)
                                        .description("유저의 로그정보. 최신순 정렬"),
                                fieldWithPath("logs[].reviewId").type(JsonFieldType.NUMBER)
                                        .description("해당 행위가 이루어진 reviewId"),
                                fieldWithPath("logs[].log").type(JsonFieldType.STRING)
                                        .description("로그"),
                                fieldWithPath("logs[].createdAt").type(JsonFieldType.STRING)
                                        .description("시간 정보")
                        )
                ));
    }

}