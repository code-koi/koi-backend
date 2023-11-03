package com.codekoi.domain.review.service;

import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.review.usecase.DeleteCodeReviewUsecase;
import com.codekoi.domain.user.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.codekoi.fixture.CodeReviewFixture.REVIEW1;
import static com.codekoi.fixture.UserFixture.SUNDO;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeleteCodeReviewTest {

    @InjectMocks
    private DeleteCodeReview deleteCodeReview;

    @Mock
    private CodeReviewRepository codeReviewRepository;

    @Test
    void 코드리뷰_삭제() {
        //given
        final long CODE_REVIEW_ID = 1L;
        final long USER_ID = 2L;
        final User user = SUNDO.toUser(USER_ID);
        final CodeReview codeReview = REVIEW1.toCodeReview(CODE_REVIEW_ID, user);

        given(codeReviewRepository.getOneById(anyLong())).willReturn(codeReview);

        //when
        deleteCodeReview.command(new DeleteCodeReviewUsecase.Command(CODE_REVIEW_ID, USER_ID));

        //then
        verify(codeReviewRepository).delete(any(CodeReview.class));
    }

    @Test
    void 내가_작성한_코드리뷰가_아니면_코드리뷰_삭제에_실패한다() {
        //given
        final long CODE_REVIEW_ID = 1L;
        final long USER_ID = 2L;
        final User user = SUNDO.toUser(USER_ID);
        final CodeReview codeReview = REVIEW1.toCodeReview(CODE_REVIEW_ID, user);

        given(codeReviewRepository.getOneById(anyLong())).willReturn(codeReview);

        //when
        final long ANOTHER_USER_ID = 3L;
        deleteCodeReview.command(new DeleteCodeReviewUsecase.Command(CODE_REVIEW_ID, ANOTHER_USER_ID));

        //then
        verify(codeReviewRepository, never()).delete(any(CodeReview.class));
    }
}