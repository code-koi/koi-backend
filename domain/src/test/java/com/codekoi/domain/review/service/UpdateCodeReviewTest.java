package com.codekoi.domain.review.service;

import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.review.usecase.UpdateCodeReviewUsecase;
import com.codekoi.domain.skill.skill.Skill;
import com.codekoi.domain.skill.skill.SkillRepository;
import com.codekoi.domain.user.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.codekoi.fixture.CodeReviewFixture.REVIEW1;
import static com.codekoi.fixture.CodeReviewFixture.REVIEW2;
import static com.codekoi.fixture.SkillFixture.JPA;
import static com.codekoi.fixture.UserFixture.SUNDO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UpdateCodeReviewTest {

    @InjectMocks
    private UpdateCodeReview updateCodeReview;

    @Mock
    private CodeReviewRepository codeReviewRepository;

    @Mock
    private SkillRepository skillRepository;


    @Test
    void 코드리뷰를_변경한다() {
        //given
        final Long CODE_REVIEW_ID = 1L;
        final Long USER_ID = 2L;
        final User user = SUNDO.toUser(USER_ID);
        final CodeReview codeReview = REVIEW1.toCodeReview(CODE_REVIEW_ID, user);

        final Long SKILL_ID = 3L;
        final Skill skill = JPA.toSkill(SKILL_ID);

        given(codeReviewRepository.getOneById(anyLong())).willReturn(codeReview);
        given(skillRepository.findAllById(anyList())).willReturn(List.of(skill));

        //when
        final String UPDATE_TITLE = REVIEW2.title;
        final String UPDATE_CONTENT = REVIEW2.content;
        final List<Long> UPDATE_SKILL_IDS = List.of(SKILL_ID);
        updateCodeReview.command(new UpdateCodeReviewUsecase.Command(CODE_REVIEW_ID, USER_ID, UPDATE_TITLE, UPDATE_CONTENT, UPDATE_SKILL_IDS));

        //then
        assertThat(codeReview.getTitle()).isEqualTo(UPDATE_TITLE);
        assertThat(codeReview.getContent()).isEqualTo(UPDATE_CONTENT);

        assertThat(codeReview.getSkills()).hasSize(1);
        assertThat(codeReview.getSkills()).extracting("skill").extracting("id")
                .containsExactlyInAnyOrder(SKILL_ID);
    }

    @Test
    void 내가_작성한_코드리뷰가_아니면_코드리뷰_변경에_실패한다() {
        //given
        final Long CODE_REVIEW_ID = 1L;
        final Long USER_ID = 2L;
        User user = SUNDO.toUser(USER_ID);
        final CodeReview codeReview = REVIEW1.toCodeReview(CODE_REVIEW_ID, user);

        given(codeReviewRepository.getOneById(anyLong())).willReturn(codeReview);

        //when
        final Long ANOTHER_USER_ID = 3L;
        updateCodeReview.command(new UpdateCodeReviewUsecase.Command(CODE_REVIEW_ID, ANOTHER_USER_ID, "", "", List.of()));

        //then
        verify(skillRepository, never()).findAllById(anyList());
    }
}