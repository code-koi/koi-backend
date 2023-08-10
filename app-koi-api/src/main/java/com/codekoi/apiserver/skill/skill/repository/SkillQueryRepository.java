package com.codekoi.apiserver.skill.skill.repository;


import com.codekoi.domain.skill.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillQueryRepository extends JpaRepository<Skill, Long> {

    @Query("select s from Skill s order by s.searchCount desc limit 10")
    List<Skill> findTop10();
}
