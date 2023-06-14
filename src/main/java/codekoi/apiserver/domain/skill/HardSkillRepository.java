package codekoi.apiserver.domain.skill;

import codekoi.apiserver.domain.skill.doamin.HardSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HardSkillRepository extends JpaRepository<HardSkill, Long> {
}
