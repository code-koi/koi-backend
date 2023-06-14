package codekoi.apiserver.docs;

import codekoi.apiserver.domain.user.controller.UserApiController;
import codekoi.apiserver.domain.user.dto.UserDetail;
import codekoi.apiserver.utils.ControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static codekoi.apiserver.utils.fixture.UserFixture.SUNDO;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@WebMvcTest(controllers = {
        UserApiController.class
})
public class UserApiDocsControllerTest extends ControllerTest {

    protected ObjectMapper mapper = new ObjectMapper();

    protected MockMvc mvc;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
        this.mvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .addFilter(new CharacterEncodingFilter("UTF-8", true))
                        .apply(documentationConfiguration(provider)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint()))
                        .alwaysDo(print())
                        .build();
    }


    @Test
    @DisplayName("유저 상세조회")
    void userDetail() throws Exception {
        final UserDetail.Skill skill = new UserDetail.Skill(1L, "JPA");
        final UserDetail dto = new UserDetail(true, SUNDO.nickname, SUNDO.email, SUNDO.profileImageUrl, SUNDO.years, SUNDO.introduce,
                new UserDetail.Activity(1), List.of(skill));

        given(userQuery.gerUserDetail(anyLong(), anyLong()))
                .willReturn(dto);

        final ResultActions result = mvc.perform(get("/api/users/1"));

        result
                .andExpect(status().isOk())
                .andDo(document("users/get",
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
                                        .description("자기소개"),
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
}
