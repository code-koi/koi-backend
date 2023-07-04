package codekoi.apiserver.domain.skill;

import codekoi.apiserver.domain.skill.doamin.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HardSkillRepository extends JpaRepository<Skill, Long> {
}
