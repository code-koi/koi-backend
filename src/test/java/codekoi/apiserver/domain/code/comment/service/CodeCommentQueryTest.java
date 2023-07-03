package codekoi.apiserver.domain.code.comment.service;


import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.comment.dto.CodeCommentDetailDto;
import codekoi.apiserver.domain.code.comment.dto.UserCodeCommentDto;
import codekoi.apiserver.domain.code.comment.repository.CodeReviewCommentRepository;
import codekoi.apiserver.domain.code.like.domain.Like;
import codekoi.apiserver.domain.code.like.repository.LikeRepository;
import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.repository.CodeReviewRepository;
import codekoi.apiserver.domain.koi.history.domain.KoiHistory;
import codekoi.apiserver.domain.koi.history.repository.KoiHistoryRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.dto.UserProfileDto;
import codekoi.apiserver.domain.user.repository.UserRepository;
import codekoi.apiserver.utils.ServiceTest;
import codekoi.apiserver.utils.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static codekoi.apiserver.utils.fixture.CodeReviewCommentFixture.REVIEW_COMMENT;
import static codekoi.apiserver.utils.fixture.CodeReviewFixture.REVIEW;
import static codekoi.apiserver.utils.fixture.KoiHistoryFixture.RIVER;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

class CodeCommentQueryTest extends ServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CodeReviewCommentRepository commentRepository;

    @Autowired
    KoiHistoryRepository koiHistoryRepository;

    @Autowired
    CodeReviewRepository codeReviewRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    CodeCommentQuery commentQuery;

    private User user1;
    private User user2;
    private CodeReview codeReview;
    private CodeReviewComment reviewComment;
    private KoiHistory koiHistory;
    private Like like;

    @BeforeEach
    public void init() {
        user1 = UserFixture.SUNDO.toUser(); //리뷰를 요청한 사람
        user2 = UserFixture.HONG.toUser(); //리뷰에 답변 남긴 사람
        userRepository.saveAll(List.of(user1, user2));

        codeReview = CodeReview.of(user1, REVIEW.title, REVIEW.content);
        codeReviewRepository.save(codeReview);

        reviewComment = CodeReviewComment.of(user2, codeReview, REVIEW_COMMENT.content);
        commentRepository.save(reviewComment);

        koiHistory = KoiHistory.of(user2, user1, reviewComment, RIVER.koiType, RIVER.message);
        koiHistoryRepository.save(koiHistory);

        like = Like.of(user1, reviewComment);
        likeRepository.save(like);
    }

    @Test
    @DisplayName("유저가 남긴 리뷰 댓글 목록 조회")
    void userComments() {
        //when
        final List<UserCodeCommentDto> userComments = commentQuery.getUserComments(user2.getId());

        //then
        assertThat(userComments).hasSize(1);

        final UserCodeCommentDto dto = userComments.get(0);

        final UserProfileDto userDto = dto.getUser();
        assertThat(userDto.getId()).isEqualTo(user2.getId());
        assertThat(userDto.getNickname()).isEqualTo(user2.getNickname());
        assertThat(userDto.getProfileImageUrl()).isEqualTo(user2.getProfileImageUrl());

        assertThat(dto.getReviewId()).isEqualTo(codeReview.getId());
        assertThat(dto.getContent()).isEqualTo(reviewComment.getContent());
        assertThat(dto.getKoiType()).isEqualTo(koiHistory.getKoiType());
        assertThat(dto.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("리뷰 상세페이지의 댓글 목록 조회")
    void commentsOnReviewDetail() {
        Long sessionUserId = 999L;

        //when
        final List<CodeCommentDetailDto> comments = commentQuery.getCommentsOnReview(sessionUserId, codeReview.getId());

        assertThat(comments).hasSize(1);
        final CodeCommentDetailDto dto = comments.get(0);

        final UserProfileDto userDto = dto.getUser();
        assertThat(userDto.getId()).isEqualTo(user2.getId());
        assertThat(userDto.getNickname()).isEqualTo(user2.getNickname());
        assertThat(userDto.getProfileImageUrl()).isEqualTo(user2.getProfileImageUrl());

        assertThat(dto.getId()).isEqualTo(reviewComment.getId());
        assertThat(dto.getContent()).isEqualTo(reviewComment.getContent());
        assertThat(dto.getKoiType()).isEqualTo(koiHistory.getKoiType());
        assertThat(dto.getMe()).isFalse();
        assertThat(dto.isLiked()).isFalse();
        assertThat(dto.getLikeCount()).isEqualTo(1);
    }
}