package com.codekoi.apiserver.skill.review.service;

import com.codekoi.apiserver.comment.repository.ReviewCommentRepository;
import com.codekoi.apiserver.review.repository.CodeReviewRepository;
import com.codekoi.apiserver.skill.review.dto.UserSkillStatistics;
import com.codekoi.apiserver.skill.review.repository.CodeReviewSkillRepository;
import com.codekoi.domain.comment.entity.ReviewComment;
import com.codekoi.domain.review.entity.CodeReview;
import com.codekoi.domain.skill.review.entity.CodeReviewSkill;
import com.codekoi.domain.skill.skill.entity.Skill;
import com.codekoi.domain.user.entity.User;
import com.codekoi.domain.user.repository.UserCoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CodeReviewSkillQueryService {

    private final CodeReviewRepository codeReviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final CodeReviewSkillRepository codeReviewSkillRepository;

    private final UserCoreRepository userCoreRepository;

    public List<UserSkillStatistics> findUserSkillStatistics(Long userId) {
        final User user = userCoreRepository.getOneById(userId);

        final List<CodeReview> reviews = codeReviewRepository.findByUserId(user.getId());
        final List<ReviewComment> comments = reviewCommentRepository.findByUserId(user.getId());

        final List<CodeReviewSkill> codeReviewSkills = getCodeReviewSkills(reviews, comments);
        final Map<Skill, Long> countMap = getCountMap(reviews, comments, codeReviewSkills);

        final Set<Skill> skillSet = codeReviewSkills.stream()
                .map(CodeReviewSkill::getSkill)
                .collect(Collectors.toSet());

        return skillSet.stream()
                .map(s -> UserSkillStatistics.of(s, countMap.get(s).intValue()))
                .sorted(Comparator.comparingInt(UserSkillStatistics::getCount).reversed())
                .collect(Collectors.toList());
    }

    private List<CodeReviewSkill> getCodeReviewSkills(List<CodeReview> reviews, List<ReviewComment> comments) {
        final List<Long> codeReviewIds = Stream.concat(
                        reviews.stream(),
                        comments.stream()
                                .map(ReviewComment::getCodeReview)
                ).map(CodeReview::getId)
                .toList();

        return codeReviewSkillRepository.findByCodeReviewIdIn(codeReviewIds);
    }


    private Map<Skill, Long> getCountMap(List<CodeReview> reviews, List<ReviewComment> comments, List<CodeReviewSkill> codeReviewSkills) {
        //key: 코드리뷰 아이디. value: 유저가 남긴 코드리뷰 댓글
        final Map<Long, List<ReviewComment>> commentsMapByReviewId = comments.stream()
                .collect(Collectors.groupingBy(c -> c.getCodeReview().getId()));

        final Map<Long, List<CodeReviewSkill>> codeReviewSkillsMap = codeReviewSkills.stream()
                .collect(Collectors.groupingBy(c -> c.getCodeReview().getId()));

        return Stream.concat(
                        reviews.stream()
                                //만일, 유저가 같은 코드리뷰에 대해 댓글도 남겼으면 중복 제거
                                .map(CodeReview::getId)
                                .filter(id -> !commentsMapByReviewId.containsKey(id)),
                        commentsMapByReviewId.values()
                                .stream()
                                //하나의 게시글에 여러개의 댓글을 남기는 경우. 한 개만 통계에 반영한다.
                                .map(c -> c.get(0).getCodeReview().getId())

                ).map(codeReviewSkillsMap::get)
                .flatMap(List::stream)
                .map(CodeReviewSkill::getSkill)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
    }
}
