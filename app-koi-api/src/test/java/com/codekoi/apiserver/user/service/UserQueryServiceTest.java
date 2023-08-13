package com.codekoi.apiserver.user.service;

import com.codekoi.apiserver.comment.repository.ReviewCommentQueryRepository;
import com.codekoi.apiserver.user.dto.UserDetail;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.codekoi.fixture.UserFixture.SUNDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserQueryServiceTest {

    @Mock
    private ReviewCommentQueryRepository reviewCommentQueryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryService userQueryService;

    @Test
    void 유저_상세_페이지_조회에_성공한다() {
        //given
        final User user = SUNDO.toUser(1L);
        given(userRepository.getOneById(any()))
                .willReturn(user);

        given(reviewCommentQueryRepository.countByUserId(any()))
                .willReturn(1);


        Long sessionUserId = user.getId(); //동일 인물

        //when
        final UserDetail userDetail = userQueryService.getUserDetail(sessionUserId, user.getId());

        //then
        assertThat(userDetail.isMe()).isEqualTo(true);
        assertThat(userDetail.getNickname()).isEqualTo(user.getNickname());
        assertThat(userDetail.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDetail.getProfileImageUrl()).isEqualTo(user.getProfileImageUrl());
        assertThat(userDetail.getYears()).isEqualTo(user.getYears());
        assertThat(userDetail.getIntroduce()).isEqualTo(user.getIntroduce());
        assertThat(userDetail.getActivity().getCodeReview()).isEqualTo(1);
//        assertThat(userDetail.getSkills()).hasSize(1)
//                .extracting("name", "id")
//                .containsExactly(tuple(skill.getName(), skill.getId()));

    }

}