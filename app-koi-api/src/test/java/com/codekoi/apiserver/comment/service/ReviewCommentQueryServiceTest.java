package com.codekoi.apiserver.comment.service;

import com.codekoi.apiserver.comment.dto.CommentReviewDetailDto;
import com.codekoi.apiserver.comment.dto.UserCodeCommentDto;
import com.codekoi.apiserver.user.dto.UserProfileDto;
import com.codekoi.domain.comment.ReviewComment;
import com.codekoi.domain.comment.ReviewCommentRepository;
import com.codekoi.domain.koi.KoiHistory;
import com.codekoi.domain.koi.KoiHistoryRepository;
import com.codekoi.domain.like.Like;
import com.codekoi.domain.like.LikeRepository;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
import com.codekoi.fixture.CodeReviewFixture;
import com.codekoi.fixture.UserFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.codekoi.fixture.KoiHistoryFixture.SEA;
import static com.codekoi.fixture.ReviewCommentFixture.REVIEW_COMMENT;
import static com.codekoi.fixture.UserFixture.SUNDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReviewCommentQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewCommentRepository reviewCommentRepository;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private KoiHistoryRepository koiHistoryRepository;
    @Mock
    private CodeReviewRepository codeReviewRepository;

    @InjectMocks
    private ReviewCommentQueryService reviewCommentQueryService;


    @Test
    void 유저의_댓글_목록_조회() {
        //given
        final User user = SUNDO.toUser(1L);
        given(userRepository.getOneById(any()))
                .willReturn(user);

        final CodeReview codeReview = CodeReviewFixture.REVIEW1.toCodeReview(2L, user);
        final ReviewComment reviewComment = REVIEW_COMMENT.toCodeReviewComment(3L, user, codeReview);

        given(reviewCommentRepository.findByUserId(any()))
                .willReturn(List.of(reviewComment));

        final User source = UserFixture.HONG.toUser(4L);
        final KoiHistory koiHistory = SEA.toKoiHistory(4L, user, source, reviewComment);
        given(koiHistoryRepository.findUserCommentKoiHistory(any()))
                .willReturn(List.of(koiHistory));

        final Like like = Like.of(source, reviewComment);
        given(likeRepository.findByCommentIdIn(any()))
                .willReturn(List.of(like));

        //when
        final List<UserCodeCommentDto> comments = reviewCommentQueryService.getUserComments(user.getId());

        //then
        final UserCodeCommentDto dto = comments.get(0);

        final UserProfileDto userDto = dto.getUser();
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getNickname()).isEqualTo(user.getNickname());
        assertThat(userDto.getProfileImageUrl()).isEqualTo(user.getProfileImageUrl());

        assertThat(dto.getReviewId()).isEqualTo(codeReview.getId());
        assertThat(dto.getContent()).isEqualTo(reviewComment.getContent());
        assertThat(dto.getKoiType()).isEqualTo(koiHistory.getKoiType());
        assertThat(dto.getLikeCount()).isEqualTo(1);
    }

    @Test
    void 리뷰_요청의_댓글_목록_조회하기() {
        //given
        final User user = SUNDO.toUser(1L);
        final CodeReview codeReview = CodeReviewFixture.REVIEW1.toCodeReview(2L, user);
        given(codeReviewRepository.getOneById(any()))
                .willReturn(codeReview);

        final ReviewComment reviewComment = REVIEW_COMMENT.toCodeReviewComment(3L, user, codeReview);
        given(reviewCommentRepository.findByCodeReviewId(any()))
                .willReturn(List.of(reviewComment));

        final Like like = Like.of(user, reviewComment);
        given(likeRepository.findByCommentIdIn(any()))
                .willReturn(List.of(like));

        final KoiHistory koiHistory = SEA.toKoiHistory(1L, user, UserFixture.HONG.toUser(), reviewComment);
        given(koiHistoryRepository.findUserCommentKoiHistory(any()))
                .willReturn(List.of(koiHistory));

        //when
        final List<CommentReviewDetailDto> comments = reviewCommentQueryService.getCommentsOnReview(user.getId(), codeReview.getId());

        //then
        assertThat(comments).hasSize(1);
        final CommentReviewDetailDto dto = comments.get(0);

        final UserProfileDto userDto = dto.getUser();
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getNickname()).isEqualTo(user.getNickname());
        assertThat(userDto.getProfileImageUrl()).isEqualTo(user.getProfileImageUrl());

        assertThat(dto.getId()).isEqualTo(reviewComment.getId());
        assertThat(dto.getContent()).isEqualTo(reviewComment.getContent());
        assertThat(dto.getKoiType()).isEqualTo(koiHistory.getKoiType());
        assertThat(dto.getMe()).isTrue();
        assertThat(dto.isLiked()).isFalse();
        assertThat(dto.getLikeCount()).isEqualTo(1);
    }
}