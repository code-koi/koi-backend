package com.codekoi.apiserver.review.service;

import com.codekoi.domain.review.usecase.CreateCodeReviewUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CodeReviewService {

    private final CreateCodeReviewUsecase createCodeReviewUsecase;

    public Long create(Long userId, String title, String content, List<Long> skillIds) {
        return createCodeReviewUsecase.command(new CreateCodeReviewUsecase.Command(userId, title, content, skillIds));
    }
}
