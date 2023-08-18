package com.codekoi.apiserver.skill.skill.controller;

import com.codekoi.apiserver.skill.skill.controller.response.TopSkillRankResponse;
import com.codekoi.apiserver.skill.skill.dto.SkillInfo;
import com.codekoi.apiserver.skill.skill.service.SkillQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillRestController {

    private final SkillQueryService skillQueryService;

    @GetMapping
    public TopSkillRankResponse getAllSkill() {
        final List<SkillInfo> skill = skillQueryService.getAllSkill();
        return new TopSkillRankResponse(skill);
    }

    @GetMapping("/rank")
    public TopSkillRankResponse getTopSkillRank() {
        final List<SkillInfo> skillRank = skillQueryService.getSkillRank();

        return new TopSkillRankResponse(skillRank);
    }

}
