package codekoi.apiserver.domain.code.review.service;


import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.comment.repository.CodeReviewCommentRepository;
import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.CodeReviewStatus;
import codekoi.apiserver.domain.code.review.domain.Favorite;
import codekoi.apiserver.domain.code.review.dto.CodeReviewDetailDto;
import codekoi.apiserver.domain.code.review.dto.HotCodeReview;
import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import codekoi.apiserver.domain.code.review.dto.UserSkillStatistics;
import codekoi.apiserver.domain.code.review.repository.CodeFavoriteRepository;
import codekoi.apiserver.domain.code.review.repository.CodeReviewRepository;
import codekoi.apiserver.domain.skill.HardSkillRepository;
import codekoi.apiserver.domain.skill.doamin.Skill;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.dto.UserProfileDto;
import codekoi.apiserver.domain.user.repository.UserRepository;
import codekoi.apiserver.utils.EntityReflectionTestUtil;
import codekoi.apiserver.utils.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static codekoi.apiserver.utils.fixture.CodeReviewCommentFixture.REVIEW_COMMENT;
import static codekoi.apiserver.utils.fixture.CodeReviewFixture.REVIEW;
import static codekoi.apiserver.utils.fixture.SkillFixture.JPA;
import static codekoi.apiserver.utils.fixture.SkillFixture.SPRING;
import static codekoi.apiserver.utils.fixture.UserFixture.HONG;
import static codekoi.apiserver.utils.fixture.UserFixture.SUNDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class CodeReviewQueryTest extends ServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CodeReviewRepository codeReviewRepository;
    @Autowired
    HardSkillRepository hardSkillRepository;
    @Autowired
    CodeFavoriteRepository favoriteRepository;
    @Autowired
    CodeReviewCommentRepository commentRepository;

    @Autowired
    CodeReviewQuery codeReviewQuery;

    @PersistenceContext
    EntityManager em;

    private User user;
    private CodeReview codeReview;
    private Skill skill1;
    private Skill skill2;

    @BeforeEach
    public void init() {
        user = SUNDO.toUser();
        userRepository.save(user);

        codeReview = CodeReview.of(user, REVIEW.title, REVIEW.content);
        EntityReflectionTestUtil.setCreatedAt(codeReview, LocalDateTime.now());

        skill1 = JPA.toHardSkill();
        skill2 = SPRING.toHardSkill();
        hardSkillRepository.saveAll(List.of(skill1, skill2));

        codeReview.addCodeReviewSkill(skill1);
        codeReview.addCodeReviewSkill(skill2);

        codeReviewRepository.save(codeReview);
    }

    @DisplayName("userId로 남긴 코드리뷰 목록 조회하기 테스트")
    @Nested
    class UserCodeReviewList {

        @Test
        @DisplayName("세션유저의 유저 상세페이지를 조회하는 경우, 즐겨찾기한 상태를 같이 보여준다.")
        void getMyUserDetail() {
            //given
            final Favorite favorite = Favorite.of(codeReview, user);
            favoriteRepository.save(favorite);

            clearPersistenceContext();

            Long sessionUserId = user.getId();

            //when
            final List<UserCodeReviewDto> reviews = codeReviewQuery.findRequestedCodeReviews(sessionUserId, user.getId());

            //then
            assertThat(reviews).hasSize(1);

            final UserCodeReviewDto review = reviews.get(0);
            assertThat(review.getTitle()).isEqualTo(REVIEW.title);
            assertThat(review.getSkills()).containsExactlyInAnyOrder(JPA.name, SPRING.name);
            assertThat(review.getStatus()).isEqualTo(CodeReviewStatus.PENDING);
            assertThat(review.getReviewId()).isEqualTo(codeReview.getId());
            assertThat(review.getIsFavorite()).isTrue();

            final UserProfileDto reviewUser = review.getUser();
            assertThat(reviewUser.getId()).isEqualTo(user.getId());
            assertThat(reviewUser.getProfileImageUrl()).isEqualTo(SUNDO.profileImageUrl);
            assertThat(reviewUser.getNickname()).isEqualTo(SUNDO.nickname);
        }

        @Test
        @DisplayName("세션유저와 다른 유저 상세페이지를 조회하는 경우, 즐겨찾기한 상태가 포함되지 않는다.")
        void getOtherUserDetail() {
            //given
            clearPersistenceContext();

            Long sessionUserId = 99999L;

            //when
            final List<UserCodeReviewDto> reviews = codeReviewQuery.findRequestedCodeReviews(sessionUserId, user.getId());

            //then
            assertThat(reviews).hasSize(1);

            final UserCodeReviewDto review = reviews.get(0);
            assertThat(review.getTitle()).isEqualTo(REVIEW.title);
            assertThat(review.getSkills()).containsExactlyInAnyOrder(JPA.name, SPRING.name);
            assertThat(review.getStatus()).isEqualTo(CodeReviewStatus.PENDING);
            assertThat(review.getReviewId()).isEqualTo(codeReview.getId());
            assertThat(review.getIsFavorite()).isFalse();

            final UserProfileDto reviewUser = review.getUser();
            assertThat(reviewUser.getId()).isEqualTo(user.getId());
            assertThat(reviewUser.getProfileImageUrl()).isEqualTo(SUNDO.profileImageUrl);
            assertThat(reviewUser.getNickname()).isEqualTo(SUNDO.nickname);
        }
    }


    @Test
    @DisplayName("코드 리뷰 즐겨찾기 목록 조회")
    void findUserFavoriteCodeReviews() {
        //given
        final User hong = HONG.toUser();
        userRepository.save(hong);
        final Favorite favorite = Favorite.of(codeReview, hong);
        favoriteRepository.save(favorite);

        clearPersistenceContext();

        Long sessionUserId = hong.getId();

        //when
        final List<UserCodeReviewDto> reviews = codeReviewQuery.findFavoriteCodeReviews(sessionUserId, hong.getId());

        //then
        assertThat(reviews).hasSize(1);

        final UserCodeReviewDto review = reviews.get(0);
        assertThat(review.getTitle()).isEqualTo(REVIEW.title);
        assertThat(review.getSkills()).containsExactlyInAnyOrder(JPA.name, SPRING.name);
        assertThat(review.getStatus()).isEqualTo(CodeReviewStatus.PENDING);
        assertThat(review.getReviewId()).isEqualTo(codeReview.getId());
        assertThat(review.getIsFavorite()).isTrue();

        final UserProfileDto reviewUser = review.getUser();
        assertThat(reviewUser.getId()).isEqualTo(user.getId());
        assertThat(reviewUser.getProfileImageUrl()).isEqualTo(SUNDO.profileImageUrl);
        assertThat(reviewUser.getNickname()).isEqualTo(SUNDO.nickname);

    }

    @Test
    @DisplayName("코드리뷰 상세 테스트")
    void codeReviewDetail() {
        //given
        Long sessionUserId = 999L;

        clearPersistenceContext();

        //when
        final CodeReviewDetailDto review = codeReviewQuery.findCodeReviewDetail(sessionUserId, codeReview.getId());

        //then
        assertThat(review.getTitle()).isEqualTo(REVIEW.title);
        assertThat(review.getSkills()).containsExactlyInAnyOrder(JPA.name, SPRING.name);
        assertThat(review.getStatus()).isEqualTo(CodeReviewStatus.PENDING);
        assertThat(review.getIsFavorite()).isFalse();
        assertThat(review.getMe()).isFalse();

        final UserProfileDto reviewUser = review.getUser();
        assertThat(reviewUser.getId()).isEqualTo(user.getId());
        assertThat(reviewUser.getProfileImageUrl()).isEqualTo(SUNDO.profileImageUrl);
        assertThat(reviewUser.getNickname()).isEqualTo(SUNDO.nickname);
    }

    @Nested
    @DisplayName("유저의 스킬 태그 통계 조회 테스트")
    class UserSkillStatisticsTest {

        @Test
        @DisplayName("코드리뷰를 요청하고, 다른 리뷰건에 댓글을 남기면 통계에 둘 다 반영된다.")
        void ReviewOtherComment() {
            //given
            final User hong = HONG.toUser();
            userRepository.save(hong);
            final CodeReview otherReview = REVIEW.toCodeReview(hong);

            final Skill skill = SPRING.toHardSkill();
            hardSkillRepository.save(skill);
            otherReview.addCodeReviewSkill(skill);
            codeReviewRepository.save(otherReview);

            final CodeReviewComment comment = REVIEW_COMMENT.toCodeReviewComment(user, otherReview);
            commentRepository.save(comment);

            clearPersistenceContext();

            //when
            final List<UserSkillStatistics> statistics = codeReviewQuery.findUserSkillStatistics(user.getId());

            //then
            assertThat(statistics).hasSize(3);
        }

        @Test
        @DisplayName("코드리뷰와 댓글 둘 다 남기면, 하나만 통계에 반영한다.")
        void sameReviewComment() {
            //given
            final CodeReviewComment c1 = REVIEW_COMMENT.toCodeReviewComment(user, codeReview);
            commentRepository.save(c1);

            clearPersistenceContext();

            //when
            final List<UserSkillStatistics> statistics = codeReviewQuery.findUserSkillStatistics(user.getId());

            //then
            assertThat(statistics).hasSize(2);
            assertThat(statistics)
                    .extracting("id", "name", "count")
                    .containsExactlyInAnyOrder(
                            tuple(skill2.getId(), skill2.getName(), 1),
                            tuple(skill1.getId(), skill1.getName(), 1)
                    );
        }

        @Test
        @DisplayName("하나의 코드리뷰에 여러개의 댓글을 남기면, 하나의 댓글만 통계에 반영한다.")
        void oneReviewMultiComment() {
            //given
            final User hong = HONG.toUser();
            userRepository.save(hong);

            final CodeReviewComment c1 = REVIEW_COMMENT.toCodeReviewComment(hong, codeReview);
            final CodeReviewComment c2 = REVIEW_COMMENT.toCodeReviewComment(hong, codeReview);
            commentRepository.saveAll(List.of(c1, c2));

            clearPersistenceContext();

            //when
            final List<UserSkillStatistics> statistics = codeReviewQuery.findUserSkillStatistics(hong.getId());

            //then
            assertThat(statistics).hasSize(2);
        }
    }

    @Test
    @DisplayName("즐겨찾기 순으로 코드리뷰 요청 목록을 정렬하여 조회한다.")
    @Disabled
    void favoriteCodeReviewList() {

    }


    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }
}