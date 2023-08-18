package com.codekoi.apiserver.skill.skill.service;

import com.codekoi.apiserver.skill.skill.dto.SkillInfo;
import com.codekoi.domain.skill.skill.Skill;
import com.codekoi.domain.skill.skill.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillQueryService {

    private final SkillRepository skillRepository;

    public List<SkillInfo> getSkillRank() {
        final List<Skill> top10BySearchCount = skillRepository.findTop10();
        return SkillInfo.listFrom(top10BySearchCount);
    }

    public List<SkillInfo> getAllSkill() {
        final List<Skill> skills = skillRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Skill::getSearchCount).reversed())
                .toList();

        return SkillInfo.listFrom(skills);
    }

}
