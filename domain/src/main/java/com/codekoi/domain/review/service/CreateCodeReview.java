package com.codekoi.domain.review.service;

import com.codekoi.domain.review.CodeReview;
import com.codekoi.domain.review.CodeReviewRepository;
import com.codekoi.domain.review.usecase.CreateCodeReviewUsecase;
import com.codekoi.domain.skill.skill.Skill;
import com.codekoi.domain.skill.skill.SkillRepository;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateCodeReview implements CreateCodeReviewUsecase {

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
