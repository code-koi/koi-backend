package com.codekoi.domain.comment.service;

import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.comment.ReviewCommentRepository;
import com.codekoi.domain.comment.usecase.DeleteReviewCommentUseCase;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.user.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.codekoi.fixture.CodeReviewFixture.REVIEW1;
import static com.codekoi.fixture.ReviewCommentFixture.REVIEW_COMMENT;
import static com.codekoi.fixture.UserFixture.SUNDO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeleteReviewCommentTest {

    @InjectMocks
    private DeleteReviewComment deleteReviewComment;

    @Mock
    private ReviewCommentRepository reviewCommentRepository;

    @Test
    void 코드리뷰_댓글을_삭제한다() {
        //given
        final long USER_ID = 1L;
        final long COMMENT_ID = 2L;
        User user = SUNDO.toUser(USER_ID);
        CodeReview codeReview = REVIEW1.toCodeReview(user);
        ReviewComment codeReviewComment = REVIEW_COMMENT.toCodeReviewComment(COMMENT_ID, user, codeReview);

        given(reviewCommentRepository.getOneById(anyLong())).willReturn(codeReviewComment);

        //when
        deleteReviewComment.command(new DeleteReviewCommentUseCase.Command(COMMENT_ID));

        //then
        verify(reviewCommentRepository).delete(any(ReviewComment.class));
    }
}