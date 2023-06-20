package codekoi.apiserver.domain.code.review.controller;

import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import codekoi.apiserver.domain.skill.doamin.HardSkill;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.utils.ControllerTest;
import codekoi.apiserver.utils.EntityReflectionTestUtil;
import codekoi.apiserver.utils.fixture.CodeReviewFixture;
import codekoi.apiserver.utils.fixture.HardSkillFixture;
import org.junit.jupiter.api.DisplayName;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CodeReviewControllerDocsTest extends ControllerTest {

    @Test
    @DisplayName("요청한 코드리뷰 목록 조회")
    void userCodeReviewList() throws Exception {
        //given
        final User user = SUNDO.toUser(1L);

        final CodeReview codeReview = REVIEW.toCodeReview(1L, user);
        EntityReflectionTestUtil.setCreatedAt(codeReview, LocalDateTime.now());

        final HardSkill hardSkill = HardSkillFixture.JPA.toHardSkill();
        codeReview.addCodeReviewSkill(hardSkill);

        final UserCodeReviewDto dto = UserCodeReviewDto.from(codeReview);

        given(codeReviewQuery.findRequestedCodeReviewList(anyLong()))
                .willReturn(List.of(dto));

        //when
        final ResultActions result = mvc.perform(
                get("/api/code-reviews")
                        .queryParam("userId", "1")
        );

        result
                .andExpect(status().isOk())
                .andDo(document("codeReviews/get-userId",
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
                                        .description("요청한 코드리뷰 고유 아이디")
                        )
                ));
    }
}
