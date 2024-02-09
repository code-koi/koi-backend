package com.codekoi.koi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KoiHistoryRepository extends JpaRepository<KoiHistory, Long> {

    @Query("select kh from KoiHistory kh where kh.codeReviewComment.id in :id")
    List<KoiHistory> findKoiHistoryInCommentIds(@Param("id") List<Long> commentIds);

}
