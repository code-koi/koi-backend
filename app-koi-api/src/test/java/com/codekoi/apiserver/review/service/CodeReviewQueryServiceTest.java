package com.codekoi.apiserver.review.service;


import com.codekoi.apiserver.comment.repository.ReviewCommentQueryRepository;
import com.codekoi.apiserver.favorite.repository.FavoriteQueryRepository;
import com.codekoi.apiserver.like.repository.LikeQueryRepository;
import com.codekoi.apiserver.review.dto.UserCodeReviewDto;
import com.codekoi.apiserver.review.repository.CodeReviewQueryRepository;
import com.codekoi.apiserver.user.dto.UserProfileDto;
import com.codekoi.apiserver.utils.ServiceTest;
import com.codekoi.domain.favorite.Favorite;
import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.skill.skill.Skill;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static com.codekoi.fixture.CodeReviewFixture.REVIEW1;
import static com.codekoi.fixture.SkillFixture.SPRING;
import static com.codekoi.fixture.UserFixture.SUNDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class CodeReviewQueryServiceTest extends ServiceTest {


    @Mock
    private FavoriteQueryRepository favoriteQueryRepository;
    @Mock
    private CodeReviewQueryRepository codeReviewQueryRepository;
    @Mock
    private ReviewCommentQueryRepository reviewCommentQueryRepository;
    @Mock
    private LikeQueryRepository likeQueryRepository;
    @Mock
    private CodeReviewRepository codeReviewRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CodeReviewQueryService codeReviewQueryService;


    @Nested
    class 특정_유저의_코드리뷰_목록_조회하기_테스트 {

        private User user;

        private Skill skill;
        private CodeReview codeReview;

        private Favorite favorite;

        @BeforeEach
        public void init() {
            user = SUNDO.toUser(1L);

            skill = SPRING.toSkill(2L);
            codeReview = REVIEW1.toCodeReview(3L, user);
            codeReview.addCodeReviewSkill(skill);

            favorite = Favorite.of(codeReview, user);
        }

        @Test
        void 세션_유저의_상세페이지를_조회하는_경우__즐겨찾기한_상태를_같이_보여준다() {
            //given

            given(codeReviewQueryRepository.findByUserId(anyLong()))
                    .willReturn(List.of(codeReview));

            given(favoriteQueryRepository.findByUserIdAndCodeReviewIdIn(anyLong(), anyList()))
                    .willReturn(List.of(favorite));

            //when
            List<UserCodeReviewDto> reviews = codeReviewQueryService.findRequestedCodeReviews(1L, user.getId());

            //then
            assertThat(reviews).hasSize(1);

            final UserCodeReviewDto review = reviews.get(0);
            assertThat(review.getTitle()).isEqualTo(codeReview.getTitle());
            assertThat(review.getSkills()).containsExactly(skill.getName());
            assertThat(review.getStatus()).isEqualTo(codeReview.getStatus());
            assertThat(review.getReviewId()).isEqualTo(codeReview.getId());
            assertThat(review.getIsFavorite()).isTrue();

            final UserProfileDto reviewUser = review.getUser();
            assertThat(reviewUser.getId()).isEqualTo(user.getId());
            assertThat(reviewUser.getProfileImageUrl()).isEqualTo(user.getProfileImageUrl());
            assertThat(reviewUser.getNickname()).isEqualTo(user.getNickname());
        }

        @Test
        void 세션유저와_다른_유저의_상세페이지를_조회하는_경우__즐겨찾기한_상태가_포함되지_않는다() {

            //given
            Long sessionUserId = 9999L;


            given(codeReviewQueryRepository.findByUserId(anyLong()))
                    .willReturn(List.of(codeReview));

            given(favoriteQueryRepository.findByUserIdAndCodeReviewIdIn(anyLong(), anyList()))
                    .willReturn(List.of(favorite));

            //when
            final List<UserCodeReviewDto> reviews = codeReviewQueryService.findRequestedCodeReviews(sessionUserId, user.getId());


            //then
            assertThat(reviews).hasSize(1);

            final UserCodeReviewDto review = reviews.get(0);
            assertThat(review.getTitle()).isEqualTo(codeReview.getTitle());
            assertThat(review.getSkills()).containsExactly(skill.getName());
            assertThat(review.getStatus()).isEqualTo(codeReview.getStatus());
            assertThat(review.getReviewId()).isEqualTo(codeReview.getId());
            assertThat(review.getIsFavorite()).isFalse();

            final UserProfileDto reviewUser = review.getUser();
            assertThat(reviewUser.getId()).isEqualTo(user.getId());
            assertThat(reviewUser.getProfileImageUrl()).isEqualTo(user.getProfileImageUrl());
            assertThat(reviewUser.getNickname()).isEqualTo(user.getNickname());
        }
    }
}