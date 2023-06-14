package codekoi.apiserver.domain.user.repository;

import codekoi.apiserver.domain.user.domain.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

}
