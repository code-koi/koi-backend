package com.codekoi.apiserver.comment.service;

import com.codekoi.apiserver.comment.dto.CommentReviewDetailDto;
import com.codekoi.apiserver.comment.dto.HotReviewComment;
import com.codekoi.apiserver.comment.dto.UserCodeCommentDto;
import com.codekoi.apiserver.user.dto.UserProfileDto;
import com.codekoi.apiserver.utils.ServiceTest;
import com.codekoi.koi.KoiHistory;
import com.codekoi.koi.KoiHistoryRepository;
import com.codekoi.review.*;
import com.codekoi.user.User;
import com.codekoi.user.UserRepository;
import com.codekoi.user.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static com.codekoi.codereview.fixture.CodeReviewFixture.REVIEW1;
import static com.codekoi.codereview.fixture.ReviewCommentFixture.REVIEW_COMMENT;
import static com.codekoi.koi.fixture.KoiHistoryFixture.SEA;
import static com.codekoi.user.fixture.UserFixture.HONG;
import static com.codekoi.user.fixture.UserFixture.SUNDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


class ReviewCommentQueryServiceTest extends ServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewCommentRepository reviewCommentRepository;
    @Mock
    private CommentLikeRepository likeRepository;
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

        final CodeReview codeReview = REVIEW1.toCodeReview(2L, user);
        final ReviewComment reviewComment = REVIEW_COMMENT.toCodeReviewComment(3L, user, codeReview);

        given(reviewCommentRepository.findByUserId(any()))
                .willReturn(List.of(reviewComment));

        final User source = UserFixture.HONG.toUser(4L);
        final KoiHistory koiHistory = SEA.toKoiHistory(4L, user, source, reviewComment);
        given(koiHistoryRepository.findKoiHistoryInCommentIds(any()))
                .willReturn(List.of(koiHistory));

        final CommentLike like = CommentLike.of(source, reviewComment);
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
        final CodeReview codeReview = REVIEW1.toCodeReview(2L, user);
        given(codeReviewRepository.getOneById(any()))
                .willReturn(codeReview);

        final ReviewComment reviewComment = REVIEW_COMMENT.toCodeReviewComment(3L, user, codeReview);
        given(reviewCommentRepository.findByCodeReviewId(any()))
                .willReturn(List.of(reviewComment));

        final CommentLike like = CommentLike.of(user, reviewComment);
        given(likeRepository.findByCommentIdIn(any()))
                .willReturn(List.of(like));

        final KoiHistory koiHistory = SEA.toKoiHistory(1L, user, UserFixture.HONG.toUser(), reviewComment);
        given(koiHistoryRepository.findKoiHistoryInCommentIds(any()))
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

    @Test
    void 인기있는_리뷰_댓글_목록조회() {
        //given
        final User user = SUNDO.toUser(1L);
        final CodeReview codeReview = REVIEW1.toCodeReview(2L, user);
        final ReviewComment comment = REVIEW_COMMENT.toCodeReviewComment(2L, user, codeReview);
        given(reviewCommentRepository.hotCommentRank())
                .willReturn(List.of(comment));

        final User source = HONG.toUser(3L);
        final KoiHistory koiHistory = SEA.toKoiHistory(1L, user, source, comment);
        given(koiHistoryRepository.findKoiHistoryInCommentIds(any()))
                .willReturn(List.of(koiHistory));

        final CommentLike like = CommentLike.of(user, comment);
        given(likeRepository.findByUserIdAndCommentIdIn(any(), any()))
                .willReturn(List.of(like));

        //when
        final List<HotReviewComment> comments = reviewCommentQueryService.getHotComments(user.getId());

        //then
        assertThat(comments).hasSize(1);

        final HotReviewComment c = comments.get(0);
        assertThat(c.getContent()).isEqualTo(REVIEW_COMMENT.content);
        assertThat(c.getLiked()).isTrue();
        assertThat(c.getKoiType()).isEqualTo(SEA.koiType);
        assertThat(c.getLikeCount()).isEqualTo(1);

        final UserProfileDto u = c.getUser();
        assertThat(u.getId()).isEqualTo(user.getId());
        assertThat(u.getNickname()).isEqualTo(SUNDO.nickname);
        assertThat(u.getProfileImageUrl()).isEqualTo(SUNDO.profileImageUrl);

    }

}