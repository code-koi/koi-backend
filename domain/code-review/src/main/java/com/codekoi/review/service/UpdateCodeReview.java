package com.codekoi.review.service;

import com.codekoi.review.CodeReview;
import com.codekoi.review.CodeReviewRepository;
import com.codekoi.review.usecase.UpdateCodeReviewUsecase;
import com.codekoi.skill.Skill;
import com.codekoi.skill.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class UpdateCodeReview implements UpdateCodeReviewUsecase {

    private final CodeReviewRepository codeReviewRepository;
    private final SkillRepository skillRepository;

    @Override
    public void command(Command command) {
        CodeReview codeReview = codeReviewRepository.getOneById(command.codeReviewId());

        if (!codeReview.isMyCodeReview(command.userId())) {
            return;
        }
        List<Skill> skills = skillRepository.findAllById(command.skillIds());

        codeReview.update(command.title(), command.content(), skills);
    }
}
