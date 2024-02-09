package com.codekoi.review.service;

import com.codekoi.codereview.fixture.CodeReviewFixture;
import com.codekoi.codereview.fixture.ReviewCommentFixture;
import com.codekoi.review.CodeReview;
import com.codekoi.review.ReviewComment;
import com.codekoi.review.ReviewCommentRepository;
import com.codekoi.review.usecase.UpdateReviewCommentUseCase;
import com.codekoi.user.User;
import com.codekoi.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UpdateReviewCommentTest {

    @InjectMocks
    private UpdateReviewComment updateReviewComment;

    @Mock
    private ReviewCommentRepository reviewCommentRepository;

    @Test
    void 코드리뷰_댓글을_수정한다() {
        //given
        final long USER_ID = 1L;
        final long COMMENT_ID = 2L;
        final String UPDATE_CONTENT = "변경할 댓글입니다.";
        User user = UserFixture.SUNDO.toUser(USER_ID);
        CodeReview codeReview = CodeReviewFixture.REVIEW1.toCodeReview(user);
        ReviewComment codeReviewComment = ReviewCommentFixture.REVIEW_COMMENT.toCodeReviewComment(COMMENT_ID, user, codeReview);

        given(reviewCommentRepository.getOneById(anyLong())).willReturn(codeReviewComment);

        //when
        updateReviewComment.command(new UpdateReviewCommentUseCase.Command(USER_ID, COMMENT_ID, UPDATE_CONTENT));

        //then
        assertThat(codeReviewComment.getContent()).isEqualTo(UPDATE_CONTENT);
    }

    @Test
    void 본인이_작성한_코드리뷰_댓글이_아닌_경우_수정되지_않는다() {
        //given
        final long USER_ID = 1L;
        final long NOT_COMMENTED_USER_ID = 2L;
        final long COMMENT_ID = 3L;
        final String UPDATE_CONTENT = "변경할 댓글입니다.";
        User user = UserFixture.SUNDO.toUser(USER_ID);
        CodeReview codeReview = CodeReviewFixture.REVIEW1.toCodeReview(user);
        ReviewComment codeReviewComment = ReviewCommentFixture.REVIEW_COMMENT.toCodeReviewComment(user, codeReview);

        given(reviewCommentRepository.getOneById(anyLong())).willReturn(codeReviewComment);

        //when
        updateReviewComment.command(
                new UpdateReviewCommentUseCase.Command(NOT_COMMENTED_USER_ID, COMMENT_ID, UPDATE_CONTENT)
        );

        //then
        assertThat(codeReviewComment.getContent()).isNotEqualTo(UPDATE_CONTENT);
    }
}