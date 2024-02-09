package com.codekoi.apiserver.review.service;


import com.codekoi.apiserver.review.dto.BasicCodeReview;
import com.codekoi.apiserver.review.dto.CodeReviewDetailDto;
import com.codekoi.apiserver.review.dto.CodeReviewListCondition;
import com.codekoi.apiserver.review.dto.UserCodeReviewDto;
import com.codekoi.apiserver.review.repository.CodeReviewQueryRepository;
import com.codekoi.apiserver.user.dto.UserProfileDto;
import com.codekoi.apiserver.utils.ServiceTest;
import com.codekoi.favorite.Favorite;
import com.codekoi.favorite.FavoriteRepository;
import com.codekoi.pagination.NoOffSetPagination;
import com.codekoi.review.*;
import com.codekoi.skill.Skill;
import com.codekoi.user.User;
import com.codekoi.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static com.codekoi.codereview.fixture.CodeReviewFixture.REVIEW1;
import static com.codekoi.skill.fixture.SkillFixture.SPRING;
import static com.codekoi.user.fixture.UserFixture.SUNDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

class CodeReviewQueryServiceTest extends ServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private ReviewCommentRepository reviewCommentRepository;
    @Mock
    private CommentLikeRepository likeRepository;
    @Mock
    private CodeReviewRepository codeReviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CodeReviewQueryRepository codeReviewQueryRepository;

    @InjectMocks
    private CodeReviewQueryService codeReviewQueryService;

    private User user;

    private Skill skill;
    private CodeReview codeReview1;

    private Favorite favorite;

    @BeforeEach
    public void init() {
        user = SUNDO.toUser(1L);

        skill = SPRING.toSkill(2L);
        codeReview1 = REVIEW1.toCodeReview(3L, user);
        codeReview1.addCodeReviewSkill(skill);

        favorite = Favorite.of(codeReview1, user);
    }

    @Test
    void 리뷰_목록_조회하기() {
        //given
        final CodeReviewListCondition condition = new CodeReviewListCondition(CodeReviewStatus.PENDING, List.of(1L, 2L), "제목", 11L);
        given(codeReviewQueryRepository.getReviewList(any(), anyInt()))
                .willReturn(List.of(codeReview1));

        //when
        final NoOffSetPagination<BasicCodeReview, Long> page = codeReviewQueryService.getReviewList(condition);

        //then
        assertThat(page.getList()).hasSize(1);
        final BasicCodeReview dto = page.getList().get(0);
        assertThat(dto.getTitle()).isEqualTo(REVIEW1.title);
        assertThat(dto.getId()).isEqualTo(3L);
        assertThat(dto.getStatus()).isEqualTo(REVIEW1.status);
        assertThat(dto.getUser()).isEqualTo(UserProfileDto.from(user));

        assertThat(page.isHasNext()).isFalse();
        assertThat(page.getLastId()).isEqualTo(codeReview1.getId());
    }

    @Nested
    class 특정_유저의_코드리뷰_목록_조회하기_테스트 {

        @Test
        void 세션_유저의_상세페이지를_조회하는_경우__즐겨찾기한_상태를_같이_보여준다() {
            //given
            given(codeReviewRepository.findByUserId(anyLong()))
                    .willReturn(List.of(codeReview1));

            given(favoriteRepository.findByUserIdAndCodeReviewIdIn(anyLong(), anyList()))
                    .willReturn(List.of(favorite));

            //when
            List<UserCodeReviewDto> reviews = codeReviewQueryService.findRequestedCodeReviews(1L, user.getId());

            //then
            assertThat(reviews).hasSize(1);

            final UserCodeReviewDto review = reviews.get(0);
            assertThat(review.getTitle()).isEqualTo(REVIEW1.title);
            assertThat(review.getSkills()).containsExactly(SPRING.name);
            assertThat(review.getStatus()).isEqualTo(REVIEW1.status);
            assertThat(review.getReviewId()).isEqualTo(codeReview1.getId());
            assertThat(review.getIsFavorite()).isTrue();

            final UserProfileDto reviewUser = review.getUser();
            assertThat(reviewUser.getId()).isEqualTo(user.getId());
            assertThat(reviewUser.getProfileImageUrl()).isEqualTo(SUNDO.profileImageUrl);
            assertThat(reviewUser.getNickname()).isEqualTo(SUNDO.nickname);
        }

        @Test
        void 세션유저와_다른_유저의_상세페이지를_조회하는_경우__즐겨찾기한_상태가_포함되지_않는다() {
            //given
            Long sessionUserId = 9999L;


            given(codeReviewRepository.findByUserId(anyLong()))
                    .willReturn(List.of(codeReview1));

            given(favoriteRepository.findByUserIdAndCodeReviewIdIn(anyLong(), anyList()))
                    .willReturn(List.of(favorite));

            //when
            final List<UserCodeReviewDto> reviews = codeReviewQueryService.findRequestedCodeReviews(sessionUserId, user.getId());


            //then
            assertThat(reviews).hasSize(1);

            final UserCodeReviewDto review = reviews.get(0);
            assertThat(review.getTitle()).isEqualTo(codeReview1.getTitle());
            assertThat(review.getSkills()).containsExactly(skill.getName());
            assertThat(review.getStatus()).isEqualTo(codeReview1.getStatus());
            assertThat(review.getReviewId()).isEqualTo(codeReview1.getId());
            assertThat(review.getIsFavorite()).isFalse();

            final UserProfileDto reviewUser = review.getUser();
            assertThat(reviewUser.getId()).isEqualTo(user.getId());
            assertThat(reviewUser.getProfileImageUrl()).isEqualTo(user.getProfileImageUrl());
            assertThat(reviewUser.getNickname()).isEqualTo(user.getNickname());
        }
    }

    @Test
    void 코드_리뷰_즐겨찾기_목록조회() {
        //given
        given(userRepository.getOneById(anyLong()))
                .willReturn(user);

        given(favoriteRepository.findAllByUserId(anyLong()))
                .willReturn(List.of(favorite));

        Long sessionUserId = user.getId();

        //when
        final List<UserCodeReviewDto> reviews = codeReviewQueryService.findFavoriteCodeReviews(sessionUserId, user.getId());

        //then
        assertThat(reviews).hasSize(1);

        final UserCodeReviewDto review = reviews.get(0);
        assertThat(review.getTitle()).isEqualTo(REVIEW1.title);
        assertThat(review.getSkills()).containsExactly(SPRING.name);
        assertThat(review.getStatus()).isEqualTo(REVIEW1.status);
        assertThat(review.getReviewId()).isEqualTo(codeReview1.getId());
        assertThat(review.getIsFavorite()).isTrue();

        final UserProfileDto reviewUser = review.getUser();
        assertThat(reviewUser.getId()).isEqualTo(user.getId());
        assertThat(reviewUser.getProfileImageUrl()).isEqualTo(SUNDO.profileImageUrl);
        assertThat(reviewUser.getNickname()).isEqualTo(SUNDO.nickname);
    }

    @Test
    void 코드_리뷰_상세_테스트() {
        //given
        given(codeReviewRepository.getOneById(anyLong()))
                .willReturn(codeReview1);

        given(favoriteRepository.findByUserIdAndCodeReviewId(anyLong(), anyLong()))
                .willReturn(Optional.of(favorite));

        Long sessionUserId = 999L;

        //when
        final CodeReviewDetailDto review = codeReviewQueryService.findCodeReviewDetail(sessionUserId, codeReview1.getId());

        //then
        assertThat(review.getTitle()).isEqualTo(REVIEW1.title);
        assertThat(review.getSkills()).containsExactly(SPRING.name);
        assertThat(review.getStatus()).isEqualTo(REVIEW1.status);
        assertThat(review.getIsFavorite()).isTrue();
        assertThat(review.getMe()).isFalse();

        final UserProfileDto reviewUser = review.getUser();
        assertThat(reviewUser.getId()).isEqualTo(user.getId());
        assertThat(reviewUser.getProfileImageUrl()).isEqualTo(SUNDO.profileImageUrl);
        assertThat(reviewUser.getNickname()).isEqualTo(SUNDO.nickname);
    }

}