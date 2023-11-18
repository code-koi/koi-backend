package com.codekoi.domain.review.service;

import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.review.usecase.DeleteCodeReviewUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class DeleteCodeReview implements DeleteCodeReviewUsecase {

    private final CodeReviewRepository codeReviewRepository;

    @Override
    public void command(Command command) {
        CodeReview codeReview = codeReviewRepository.getOneById(command.codeReviewId());

        codeReviewRepository.delete(codeReview);
    }
}
