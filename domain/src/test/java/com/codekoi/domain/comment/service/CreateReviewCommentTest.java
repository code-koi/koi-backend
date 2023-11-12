package com.codekoi.domain.comment.service;

import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.comment.ReviewCommentRepository;
import com.codekoi.domain.comment.usecase.CreateReviewCommentUseCase;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.codekoi.fixture.CodeReviewFixture.REVIEW1;
import static com.codekoi.fixture.UserFixture.SUNDO;
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
        final long USER_ID = 1L;
        final long REVIEW_ID = 2L;

        User user = SUNDO.toUser(USER_ID);
        CodeReview codeReview = REVIEW1.toCodeReview(REVIEW_ID, user);

        given(userRepository.getOneById(anyLong())).willReturn(user);
        given(codeReviewRepository.getOneById(anyLong())).willReturn(codeReview);

        //when
        createReviewComment.command(new CreateReviewCommentUseCase.Command(REVIEW_ID, USER_ID, CONTENT));

        //then
        verify(reviewCommentRepository).save(captor.capture());
        ReviewComment reviewComment = captor.getValue();

        assertThat(reviewComment.getUser().getId()).isEqualTo(USER_ID);
        assertThat(reviewComment.getCodeReview().getId()).isEqualTo(REVIEW_ID);
        assertThat(reviewComment.getContent()).isEqualTo(CONTENT);
    }
}