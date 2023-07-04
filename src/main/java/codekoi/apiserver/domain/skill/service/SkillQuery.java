package codekoi.apiserver.domain.skill.service;

import codekoi.apiserver.domain.skill.doamin.Skill;
import codekoi.apiserver.domain.skill.dto.SkillInfo;
import codekoi.apiserver.domain.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SkillQuery {
    private final SkillRepository skillRepository;

    public List<SkillInfo> getSkillRank() {
        final List<Skill> top10BySearchCount = skillRepository.findTop10();
        return SkillInfo.listFrom(top10BySearchCount);
    }
}
