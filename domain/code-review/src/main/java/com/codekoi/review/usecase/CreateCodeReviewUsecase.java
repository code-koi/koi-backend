package com.codekoi.review.usecase;

import java.util.List;

public interface CreateCodeReviewUsecase {

    Long command(Command command);

    record Command(
            Long userId,
            String title,
            String content,
            List<Long> skillIds
    ) {

    }
}
