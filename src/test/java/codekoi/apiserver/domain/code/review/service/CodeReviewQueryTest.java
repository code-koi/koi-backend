package codekoi.apiserver.domain.code.review.service;


import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.CodeReviewStatus;
import codekoi.apiserver.domain.code.review.dto.UserCodeReviewDto;
import codekoi.apiserver.domain.code.review.repository.CodeReviewRepository;
import codekoi.apiserver.domain.skill.HardSkillRepository;
import codekoi.apiserver.domain.skill.doamin.HardSkill;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.dto.UserProfileDto;
import codekoi.apiserver.domain.user.repository.UserRepository;
import codekoi.apiserver.utils.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static codekoi.apiserver.utils.fixture.CodeReviewFixture.REVIEW;
import static codekoi.apiserver.utils.fixture.HardSkillFixture.JPA;
import static codekoi.apiserver.utils.fixture.HardSkillFixture.SPRING;
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
    CodeReviewQuery codeReviewQuery;

    @Test
    @DisplayName("userId로 남긴 코드리뷰 목록 조회하기 테스트")
    void userCodeReviewList() {
        //given
        final User user = SUNDO.toUser();
        userRepository.save(user);

        final CodeReview codeReview = CodeReview.of(user, REVIEW.title, REVIEW.content);

        final HardSkill skill1 = JPA.toHardSkill();
        final HardSkill skill2 = SPRING.toHardSkill();
        hardSkillRepository.saveAll(List.of(skill1, skill2));

        codeReview.addCodeReviewSkill(skill1);
        codeReview.addCodeReviewSkill(skill2);

        codeReviewRepository.save(codeReview);

        //when
        final List<UserCodeReviewDto> reviews = codeReviewQuery.findRequestedCodeReviewList(user.getId());
        assertThat(reviews).hasSize(1);

        final UserCodeReviewDto review = reviews.get(0);
        assertThat(review.getTitle()).isEqualTo(REVIEW.title);
        assertThat(review.getCreatedAt()).isEqualTo(codeReview.getCreatedAt().toString());
        assertThat(review.getSkills()).containsExactlyInAnyOrder(JPA.name, SPRING.name);
        assertThat(review.getStatus()).isEqualTo(CodeReviewStatus.PENDING);

        final UserProfileDto reviewUser = review.getUser();
        assertThat(reviewUser.getId()).isEqualTo(user.getId());
        assertThat(reviewUser.getProfileImageUrl()).isEqualTo(SUNDO.profileImageUrl);
        assertThat(reviewUser.getNickname()).isEqualTo(SUNDO.nickname);
    }
}