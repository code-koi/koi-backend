package com.codekoi.review.service;

import com.codekoi.review.CodeReview;
import com.codekoi.review.CodeReviewRepository;
import com.codekoi.review.usecase.DeleteCodeReviewUsecase;
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
