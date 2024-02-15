package com.codekoi.review.service;

import com.codekoi.codereview.fixture.CodeReviewFixture;
import com.codekoi.codereview.fixture.ReviewCommentFixture;
import com.codekoi.review.CodeReview;
import com.codekoi.review.CodeReviewRepository;
import com.codekoi.review.ReviewComment;
import com.codekoi.review.ReviewCommentRepository;
import com.codekoi.review.usecase.CreateReviewCommentUseCase;
import com.codekoi.user.User;
import com.codekoi.user.UserRepository;
import com.codekoi.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateReviewCommentTest {

    @InjectMocks
    private CreateReviewComment createReviewComment;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CodeReviewRepository codeReviewRepository;

    @Mock
    private ReviewCommentRepository reviewCommentRepository;

    @Captor
    private ArgumentCaptor<ReviewComment> captor;

    @Test
    void 코드리뷰에_댓글을_생성한다() {
        //given
        final String CONTENT = "댓글입니다.";
        final Long USER_ID = 1L;
        final Long REVIEW_ID = 2L;
        final Long PARENT_ID = null;

        User user = UserFixture.SUNDO.toUser(USER_ID);
        CodeReview codeReview = CodeReviewFixture.REVIEW1.toCodeReview(REVIEW_ID, user);

        given(userRepository.getOneById(anyLong())).willReturn(user);
        given(codeReviewRepository.getOneById(anyLong())).willReturn(codeReview);

        //when
        createReviewComment.command(new CreateReviewCommentUseCase.Command(REVIEW_ID, PARENT_ID, USER_ID, CONTENT));

        //then
        verify(reviewCommentRepository).save(captor.capture());
        ReviewComment reviewComment = captor.getValue();

        assertThat(reviewComment.getUser().getId()).isEqualTo(USER_ID);
        assertThat(reviewComment.getCodeReview().getId()).isEqualTo(REVIEW_ID);
        assertThat(reviewComment.getContent()).isEqualTo(CONTENT);
    }

    @Test
    void 코드리뷰에_달린_댓글에_대댓글을_생성한다() {
        //given
        final String COMMENT_CONTENT = "대댓글입니다.";
        final Long USER_ID = 1L;
        final Long REVIEW_ID = 2L;
        final Long PARENT_ID = 4L;

        User user = UserFixture.SUNDO.toUser(USER_ID);
        CodeReview codeReview = CodeReviewFixture.REVIEW1.toCodeReview(REVIEW_ID, user);
        ReviewComment parentComment = ReviewCommentFixture.REVIEW_COMMENT.toCodeReviewComment(PARENT_ID, user, codeReview);

        given(userRepository.getOneById(anyLong())).willReturn(user);
        given(codeReviewRepository.getOneById(anyLong())).willReturn(codeReview);
        given(reviewCommentRepository.getOneById(anyLong())).willReturn(parentComment);

        //when
        createReviewComment.command(new CreateReviewCommentUseCase.Command(REVIEW_ID, PARENT_ID, USER_ID, COMMENT_CONTENT));

        //then
        verify(reviewCommentRepository).save(captor.capture());
        ReviewComment childReviewComment = captor.getValue();

        assertThat(childReviewComment.getUser().getId()).isEqualTo(USER_ID);
        assertThat(childReviewComment.getCodeReview().getId()).isEqualTo(REVIEW_ID);
        assertThat(childReviewComment.getContent()).isEqualTo(COMMENT_CONTENT);
    }
}