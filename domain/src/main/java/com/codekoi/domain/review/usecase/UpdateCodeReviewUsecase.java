package com.codekoi.domain.review.usecase;

import java.util.List;

public interface UpdateCodeReviewUsecase {

    void command(Command command);

    record Command(
            Long codeReviewId,
            Long userId,
            String title,
            String content,
            List<Long> skillIds
    ) {
    }
}
