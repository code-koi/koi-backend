package codekoi.apiserver.domain.code.comment.service;


import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.comment.repository.CodeReviewCommentRepository;
import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.dto.UserCodeCommentDto;
import codekoi.apiserver.domain.code.review.repository.CodeReviewRepository;
import codekoi.apiserver.domain.koi.history.domain.KoiHistory;
import codekoi.apiserver.domain.koi.history.repository.KoiHistoryRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.dto.UserProfileDto;
import codekoi.apiserver.domain.user.repository.UserRepository;
import codekoi.apiserver.utils.ServiceTest;
import codekoi.apiserver.utils.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static codekoi.apiserver.utils.fixture.CodeReviewCommentFixture.REVIEW_COMMENT;
import static codekoi.apiserver.utils.fixture.CodeReviewFixture.REVIEW;
import static codekoi.apiserver.utils.fixture.KoiHistoryFixture.RIVER;
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
    CodeCommentQuery commentQuery;

    @Test
    @DisplayName("유저가 남긴 리뷰 댓글 목록 조회")
    void userComments() {
        //given
        final User user1 = UserFixture.SUNDO.toUser();
        final User user2 = UserFixture.HONG.toUser();
        userRepository.saveAll(List.of(user1, user2));

        final CodeReview codeReview = CodeReview.of(user1, REVIEW.title, REVIEW.content);
        codeReviewRepository.save(codeReview);

        final CodeReviewComment reviewComment = CodeReviewComment.of(user2, codeReview, REVIEW_COMMENT.content);
        commentRepository.save(reviewComment);

        final KoiHistory koiHistory = KoiHistory.of(user2, user1, reviewComment, RIVER.koiType, RIVER.message);
        koiHistoryRepository.save(koiHistory);

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
    }
}