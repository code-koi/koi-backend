package com.codekoi.skill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query("select s from Skill s order by s.searchCount desc limit 10")
    List<Skill> findTop10();
}