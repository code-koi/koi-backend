package codekoi.apiserver.domain.skill.controller;

import codekoi.apiserver.domain.skill.dto.SkillInfo;
import codekoi.apiserver.domain.skill.service.SkillQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/skills")
public class SkillController {
    private final SkillQuery skillQuery;

    @GetMapping("/rank")
    public ResponseEntity<Object> getTopSkillRank() {
        final List<SkillInfo> skillRank = skillQuery.getSkillRank();

        return ResponseEntity.ok(Map.of("skills", skillRank));
    }

}
