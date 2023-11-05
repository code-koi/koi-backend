package com.codekoi.domain.review.service;

import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.review.usecase.CreateCodeReviewUsecase;
import com.codekoi.domain.skill.skill.Skill;
import com.codekoi.domain.skill.skill.SkillRepository;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.codekoi.fixture.CodeReviewFixture.REVIEW1;
import static com.codekoi.fixture.SkillFixture.JAVA;
import static com.codekoi.fixture.SkillFixture.JPA;
import static com.codekoi.fixture.UserFixture.SUNDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CreateCodeReviewTest {

    @InjectMocks
    private CreateCodeReview createCodeReview;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CodeReviewRepository codeReviewRepository;

    @Mock
    private SkillRepository skillRepository;

    @Captor
    ArgumentCaptor<CodeReview> codeReviewCaptor;

    @Test
    void 코드리뷰를_생성한다() {
        //given
        final long USER_ID = 1L;
        User user = SUNDO.toUser(USER_ID);

        final long JPA_ID = 2L;
        final long JAVA_ID = 3L;
        final List<Long> SKILL_IDS = List.of(JPA_ID, JAVA_ID);
        List<Skill> skills = List.of(JPA.toSkill(JPA_ID), JAVA.toSkill(JAVA_ID));

        given(userRepository.getOneById(anyLong())).willReturn(user);
        given(skillRepository.findAllById(anyList())).willReturn(skills);

        //when
        final String REVIEW_TITLE = REVIEW1.title;
        final String REVIEW_CONTENT = REVIEW1.content;
        createCodeReview.command(new CreateCodeReviewUsecase.Command(USER_ID, REVIEW_TITLE, REVIEW_CONTENT, SKILL_IDS));

        //then
        verify(codeReviewRepository).save(codeReviewCaptor.capture());
        final CodeReview codeReview = codeReviewCaptor.getValue();

        assertThat(codeReview.getUser().getId()).isEqualTo(USER_ID);

        assertThat(codeReview.getTitle()).isEqualTo(REVIEW_TITLE);
        assertThat(codeReview.getContent()).isEqualTo(REVIEW_CONTENT);

        assertThat(codeReview.getSkills()).hasSize(2);
        assertThat(codeReview.getSkills()).extracting("skill").extracting("id")
                .containsExactlyInAnyOrder(JPA_ID, JAVA_ID);
    }
}