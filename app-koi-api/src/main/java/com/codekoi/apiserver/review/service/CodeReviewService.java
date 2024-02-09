package com.codekoi.apiserver.review.service;

import com.codekoi.apiserver.review.exception.CanNotDeleteCodeReviewException;
import com.codekoi.review.CodeReview;
import com.codekoi.review.CodeReviewRepository;
import com.codekoi.review.usecase.CreateCodeReviewUsecase;
import com.codekoi.review.usecase.DeleteCodeReviewUsecase;
import com.codekoi.review.usecase.UpdateCodeReviewUsecase;
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
    private final DeleteCodeReviewUsecase deleteCodeReviewUsecase;
    private final CodeReviewRepository codeReviewRepository;

    public Long create(Long userId, String title, String content, List<Long> skillIds) {
        return createCodeReviewUsecase.command(new CreateCodeReviewUsecase.Command(userId, title, content, skillIds));
    }

    public void update(Long codeReviewId, Long userId, String title, String content, List<Long> skillIds) {
        updateCodeReviewUsecase.command(new UpdateCodeReviewUsecase.Command(codeReviewId, userId, title, content, skillIds));
    }

    public void delete(Long codeReviewId, Long userId) {
        CodeReview codeReview = codeReviewRepository.getOneById(codeReviewId);
        if (!codeReview.isMyCodeReview(userId)) {
            throw new CanNotDeleteCodeReviewException();
        }
        deleteCodeReviewUsecase.command(new DeleteCodeReviewUsecase.Command(codeReviewId));
    }
}
