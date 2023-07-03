package codekoi.apiserver.domain.skill.repository;

import codekoi.apiserver.domain.skill.doamin.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query("select hs from Skill hs order by hs.searchCount desc limit 10")
    List<Skill> findTop10();
}
