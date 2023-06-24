package codekoi.apiserver.domain.user.service;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.comment.repository.CodeReviewCommentRepository;
import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.repository.CodeReviewRepository;
import codekoi.apiserver.domain.skill.HardSkillRepository;
import codekoi.apiserver.domain.skill.doamin.HardSkill;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.dto.UserDetail;
import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.domain.user.exception.UserNotFoundException;
import codekoi.apiserver.domain.user.repository.UserRepository;
import codekoi.apiserver.utils.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static codekoi.apiserver.utils.fixture.CodeReviewCommentFixture.REVIEW_COMMENT;
import static codekoi.apiserver.utils.fixture.CodeReviewFixture.REVIEW;
import static codekoi.apiserver.utils.fixture.HardSkillFixture.JPA;
import static codekoi.apiserver.utils.fixture.UserFixture.SUNDO;
import static org.assertj.core.api.Assertions.*;

class UserQueryTest extends ServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CodeReviewRepository codeReviewRepository;

    @Autowired
    CodeReviewCommentRepository commentRepository;

    @Autowired
    HardSkillRepository hardSkillRepository;

    @Autowired
    UserQuery userQuery;

    @Test
    @DisplayName("유저 조회 후, jwt로 생성할 dto로 변환한다.")
    void userAuthInfo() {
        //given
        final User user = SUNDO.toUser();
        userRepository.save(user);

        //when
        final UserToken userToken = userQuery.getUserAuth(user.getEmail());

        //then
        assertThat(userToken.getUserId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("이메일에 맞는 유저가 없으면 예외가 발생한다.")
    void hasNoUserEmail() {
        //then
        assertThatThrownBy(() -> {
            //when
            final UserToken userToken = userQuery.getUserAuth("random@abc.com");
        }).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("유저 상세 조회")
    void getUserDetail() {
        //given
        final User user = SUNDO.toUser();
        final CodeReview codeReview = REVIEW.toCodeReview(user);
        final CodeReviewComment codeReviewComment = REVIEW_COMMENT.toCodeReviewComment(user, codeReview);

        final HardSkill skill = JPA.toHardSkill();
        hardSkillRepository.save(skill);

        user.addUserSkill(skill);

        userRepository.save(user);
        codeReviewRepository.save(codeReview);
        commentRepository.save(codeReviewComment);

        Long sessionUserId = user.getId(); //동일 인물
        //when
        final UserDetail userDetail = userQuery.gerUserDetail(sessionUserId, user.getId());

        //then
        assertThat(userDetail.isMe()).isEqualTo(true);
        assertThat(userDetail.getNickname()).isEqualTo(user.getNickname());
        assertThat(userDetail.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDetail.getProfileImageUrl()).isEqualTo(user.getProfileImageUrl());
        assertThat(userDetail.getYears()).isEqualTo(user.getYears());
        assertThat(userDetail.getIntroduce()).isEqualTo(user.getIntroduce());
        assertThat(userDetail.getActivity().getCodeReview()).isEqualTo(1);
        assertThat(userDetail.getSkills()).hasSize(1)
                .extracting("name", "id")
                .containsExactly(tuple(skill.getName(), skill.getId()));
    }
}