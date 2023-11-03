package com.codekoi.apiserver.review.service;

import com.codekoi.domain.review.usecase.CreateCodeReviewUsecase;
import com.codekoi.domain.review.usecase.UpdateCodeReviewUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CodeReviewService {

    private final CreateCodeReviewUsecase createCodeReviewUsecase;
    private final UpdateCodeReviewUsecase updateCodeReviewUsecase;

    public Long create(Long userId, String title, String content, List<Long> skillIds) {
        return createCodeReviewUsecase.command(new CreateCodeReviewUsecase.Command(userId, title, content, skillIds));
    }

    public void update(Long codeReviewId, Long userId, String title, String content, List<Long> skillIds) {
        updateCodeReviewUsecase.command(new UpdateCodeReviewUsecase.Command(codeReviewId, userId, title, content, skillIds));
    }
}
