package com.codekoi.apiserver.skill.skill.service;

import com.codekoi.apiserver.skill.skill.dto.SkillInfo;
import com.codekoi.apiserver.skill.skill.repository.SkillQueryRepository;
import com.codekoi.domain.skill.skill.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillQueryService {

    private final SkillQueryRepository skillQueryRepository;

    public List<SkillInfo> getSkillRank() {
        final List<Skill> top10BySearchCount = skillQueryRepository.findTop10();
        return SkillInfo.listFrom(top10BySearchCount);
    }

}
