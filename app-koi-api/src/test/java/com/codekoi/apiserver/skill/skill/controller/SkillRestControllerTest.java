package com.codekoi.apiserver.skill.skill.controller;


import com.codekoi.apiserver.skill.skill.dto.SkillInfo;
import com.codekoi.apiserver.utils.ControllerTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

class SkillRestControllerTest extends ControllerTest {


    @Nested
    class 스킬_목록_조회_테스트 {

        @Test
        void 인기순_조회() throws Exception {
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
        void 가장_많이_사용된_스킬_건수_목록_조회() throws Exception {
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