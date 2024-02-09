package com.codekoi.review.service;

import com.codekoi.review.CodeReview;
import com.codekoi.review.CodeReviewRepository;
import com.codekoi.review.usecase.CreateCodeReviewUsecase;
import com.codekoi.skill.Skill;
import com.codekoi.skill.SkillRepository;
import com.codekoi.user.User;
import com.codekoi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class CreateCodeReview implements CreateCodeReviewUsecase {

    private final UserRepository userRepository;
    private final CodeReviewRepository codeReviewRepository;
    private final SkillRepository skillRepository;

    @Override
    public Long command(Command command) {
        final User user = userRepository.getOneById(command.userId());

        final CodeReview codeReview = CodeReview.of(user, command.title(), command.content());
        codeReviewRepository.save(codeReview);

        List<Skill> skills = skillRepository.findAllById(command.skillIds());
        skills.forEach(codeReview::addCodeReviewSkill);

        return codeReview.getId();
    }
}
