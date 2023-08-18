package com.codekoi.apiserver.skill.skill.controller;


import com.codekoi.apiserver.skill.skill.dto.SkillInfo;
import com.codekoi.apiserver.skill.skill.service.SkillQueryService;
import com.codekoi.apiserver.utils.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SkillRestController.class)
class SkillRestControllerTest extends ControllerTest {

    @MockBean
    SkillQueryService skillQueryService;


    @Nested
    @DisplayName("스킬 목록 조회 테스트")
    class SkillList {

        @Test
        @DisplayName("스킬 목록 조회 (인기순)")
        void allSkill() throws Exception {
            //given
            final SkillInfo skill = new SkillInfo("JPA", 2L);

            given(skillQueryService.getAllSkill())
                    .willReturn(List.of(skill));

            //when
            final ResultActions result = mvc.perform(get("/api/skills"));

            result
                    .andExpect(status().isOk())
                    .andDo(getSkillListDocument("skills/get"));
        }

        @Test
        @DisplayName("가장 많이 사용된 스킬 건수 목록 조회")
        void topSkillRank() throws Exception {
            //given
            final SkillInfo skill = new SkillInfo("JPA", 2L);

            given(skillQueryService.getSkillRank())
                    .willReturn(List.of(skill));

            //when
            final ResultActions result = mvc.perform(get("/api/skills/rank"));

            //then
            result
                    .andExpect(status().isOk())
                    .andDo(getSkillListDocument("skills/get-rank"));
        }

        private RestDocumentationResultHandler getSkillListDocument(String path) {
            return document(path,
                    responseFields(
                            fieldWithPath("skills").type(JsonFieldType.ARRAY)
                                    .description("스킬 목록. 인기순으로 정렬된다."),
                            fieldWithPath("skills[].name").type(JsonFieldType.STRING)
                                    .description("스킬 이름"),
                            fieldWithPath("skills[].id").type(JsonFieldType.NUMBER)
                                    .description("스킬 고유 아이디")
                    )
            );
        }
    }
}