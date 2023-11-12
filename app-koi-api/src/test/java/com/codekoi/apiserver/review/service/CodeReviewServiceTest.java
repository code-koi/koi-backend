package com.codekoi.apiserver.review.service;

import com.codekoi.apiserver.review.exception.CanNotDeleteCodeReviewException;
import com.codekoi.apiserver.utils.ServiceTest;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.user.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.codekoi.fixture.CodeReviewFixture.REVIEW1;
import static com.codekoi.fixture.UserFixture.SUNDO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class CodeReviewServiceTest extends ServiceTest {

    @InjectMocks
    private CodeReviewService codeReviewService;

    @Mock
    private CodeReviewRepository codeReviewRepository;

    @Test
    void 코드리뷰_삭제시_본인이_작성한_코드리뷰가_아니면_예외가_발생한다() {
        //given
        final long CODE_REVIEW_ID = 1L;
        final long USER_ID = 2L;
        final long ANOTHER_USER_ID = 3L;
        final User user = SUNDO.toUser(USER_ID);
        final CodeReview codeReview = REVIEW1.toCodeReview(CODE_REVIEW_ID, user);

        given(codeReviewRepository.getOneById(anyLong())).willReturn(codeReview);

        //expected
        assertThatThrownBy(() -> codeReviewService.delete(CODE_REVIEW_ID, ANOTHER_USER_ID))
                .isInstanceOf(CanNotDeleteCodeReviewException.class);
    }
}