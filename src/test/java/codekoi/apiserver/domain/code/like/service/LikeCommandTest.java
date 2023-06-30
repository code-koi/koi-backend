package codekoi.apiserver.domain.code.like.service;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.comment.repository.CodeReviewCommentRepository;
import codekoi.apiserver.domain.code.like.domain.Like;
import codekoi.apiserver.domain.code.like.exception.AlreadyLikedComment;
import codekoi.apiserver.domain.code.like.exception.LikeNotFoundException;
import codekoi.apiserver.domain.code.like.repository.LikeRepository;
import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.repository.CodeReviewRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.repository.UserRepository;
import codekoi.apiserver.utils.ServiceTest;
import codekoi.apiserver.utils.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static codekoi.apiserver.utils.fixture.CodeReviewCommentFixture.REVIEW_COMMENT;
import static codekoi.apiserver.utils.fixture.CodeReviewFixture.REVIEW;
import static org.assertj.core.api.Assertions.*;


class LikeCommandTest extends ServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CodeReviewRepository codeReviewRepository;
    @Autowired
    CodeReviewCommentRepository commentRepository;
    @Autowired
    LikeRepository likeRepository;

    @Autowired
    LikeCommand likeCommand;


    private User user;
    private CodeReview codeReview;
    private CodeReviewComment comment;

    @BeforeEach
    public void init() {
        user = UserFixture.SUNDO.toUser();
        userRepository.save(user);

        codeReview = REVIEW.toCodeReview(user);
        codeReviewRepository.save(codeReview);

        comment = REVIEW_COMMENT.toCodeReviewComment(user, codeReview);
        commentRepository.save(comment);
    }

    @Nested
    @DisplayName("좋아요 테스트")
    class Likes {
        @Test
        @DisplayName("좋아요를 시도하여 좋아요 개수를 증가시킨다.")
        void like() {
            //given

            //when
            likeCommand.like(user.getId(), comment.getId());

            //then
            final Optional<Like> likeOptional = likeRepository.findByUserIdAndCommentId(user.getId(), comment.getId());

            assertThat(likeOptional).isPresent();

            final Like like = likeOptional.get();
            assertThat(like.getUser().getId()).isEqualTo(user.getId());
            assertThat(like.getComment().getId()).isEqualTo(like.getComment().getId());
            assertThat(like.getId()).isEqualTo(comment.getLikes().get(0).getId());

            assertThat(comment.getLikeCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("이미 좋아요한 경우 예외가 발생한다.")
        void alreadyLiked() {
            //given
            likeCommand.like(user.getId(), comment.getId());

            //then
            assertThatThrownBy(() -> {
                //when
                likeCommand.like(user.getId(), comment.getId());
            }).isInstanceOf(AlreadyLikedComment.class);
        }
    }

    @Nested
    @DisplayName("좋아요 취소 테스트")
    class UnlikeTest {

        @Test
        @DisplayName("좋아요를 한 적이 없어서 예외가 발생한다.")
        void notLiked() {
            assertThatThrownBy(() -> {
                likeCommand.unlike(user.getId(), comment.getId());

            }).isInstanceOf(LikeNotFoundException.class);
        }

        @Test
        @DisplayName("좋아요 취소에 성공하여 좋아요 개수가 줄어든다.")
        void unlike() {
            //given
            likeCommand.like(user.getId(), comment.getId());

            //when
            likeCommand.unlike(user.getId(), comment.getId());

            //then
            final Optional<Like> likeOptional = likeRepository.findByUserIdAndCommentId(user.getId(), comment.getId());

            assertThat(likeOptional).isEmpty();
            assertThat(comment.getLikeCount()).isEqualTo(0);
        }
    }

}