package codekoi.apiserver.domain.code.review.service;


import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.CodeReviewStatus;
import codekoi.apiserver.domain.code.review.domain.Favorite;
import codekoi.apiserver.domain.code.review.dto.CodeReviewDetailDto;
import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static codekoi.apiserver.utils.fixture.CodeReviewFixture.REVIEW;
import static codekoi.apiserver.utils.fixture.SkillFixture.JPA;
import static codekoi.apiserver.utils.fixture.SkillFixture.SPRING;
import static codekoi.apiserver.utils.fixture.UserFixture.HONG;
import static codekoi.apiserver.utils.fixture.UserFixture.SUNDO;
import static org.assertj.core.api.Assertions.assertThat;

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
    CodeReviewQuery codeReviewQuery;

    @PersistenceContext
    EntityManager em;

    private User user;
    private CodeReview codeReview;

    @BeforeEach
    public void init() {
        user = SUNDO.toUser();
        userRepository.save(user);

        codeReview = CodeReview.of(user, REVIEW.title, REVIEW.content);
        EntityReflectionTestUtil.setCreatedAt(codeReview, LocalDateTime.now());

        final Skill skill1 = JPA.toHardSkill();
        final Skill skill2 = SPRING.toHardSkill();
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

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }
}