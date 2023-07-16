package codekoi.apiserver.domain.skill.controller;

import codekoi.apiserver.domain.skill.domain.Skill;
import codekoi.apiserver.domain.skill.dto.SkillInfo;
import codekoi.apiserver.utils.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static codekoi.apiserver.utils.fixture.SkillFixture.JPA;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SkillControllerDocsTest extends ControllerTest {

    @Test
    @DisplayName("가장 많이 사용된 스킬 건수 목록 조회")
    void topSkillRank() throws Exception {
        //given
        final Skill skill = JPA.toHardSkill(1L);
        final List<SkillInfo> skills = SkillInfo.listFrom(List.of(skill));

        given(skillQuery.getSkillRank())
                .willReturn(skills);

        //when
        final ResultActions result = mvc.perform(get("/api/skills/rank"));

        //then
        result
                .andExpect(status().isOk())
                .andDo(document("skills/get-rank",
                        responseFields(
                                fieldWithPath("skills").type(JsonFieldType.ARRAY)
                                        .description("스킬 목록. 인기순으로 정렬된다."),
                                fieldWithPath("skills[].name").type(JsonFieldType.STRING)
                                        .description("스킬 이름"),
                                fieldWithPath("skills[].id").type(JsonFieldType.NUMBER)
                                        .description("스킬 고유 아이디")
                        )
                ));
    }


}