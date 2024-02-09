package com.codekoi.review.service;

import com.codekoi.codereview.fixture.CodeReviewFixture;
import com.codekoi.codereview.fixture.ReviewCommentFixture;
import com.codekoi.review.CodeReview;
import com.codekoi.review.ReviewComment;
import com.codekoi.review.ReviewCommentRepository;
import com.codekoi.review.usecase.DeleteReviewCommentUseCase;
import com.codekoi.user.User;
import com.codekoi.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        User user = UserFixture.SUNDO.toUser(USER_ID);
        CodeReview codeReview = CodeReviewFixture.REVIEW1.toCodeReview(user);
        ReviewComment codeReviewComment = ReviewCommentFixture.REVIEW_COMMENT.toCodeReviewComment(COMMENT_ID, user, codeReview);

        given(reviewCommentRepository.getOneById(anyLong())).willReturn(codeReviewComment);

        //when
        deleteReviewComment.command(new DeleteReviewCommentUseCase.Command(COMMENT_ID));

        //then
        verify(reviewCommentRepository).delete(any(ReviewComment.class));
    }
}