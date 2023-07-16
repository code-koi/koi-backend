package codekoi.apiserver.domain.code.review.service;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.comment.repository.CodeReviewCommentRepository;
import codekoi.apiserver.domain.code.like.domain.Like;
import codekoi.apiserver.domain.code.like.repository.LikeRepository;
import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.review.domain.CodeReviewSkill;
import codekoi.apiserver.domain.code.review.domain.Favorite;
import codekoi.apiserver.domain.code.review.dto.*;
import codekoi.apiserver.domain.code.review.exception.CodeReviewNotFoundException;
import codekoi.apiserver.domain.code.review.repository.CodeFavoriteRepository;
import codekoi.apiserver.domain.code.review.repository.CodeReviewRepository;
import codekoi.apiserver.domain.code.review.repository.CodeReviewSkillRepository;
import codekoi.apiserver.domain.code.review.vo.Activity;
import codekoi.apiserver.domain.code.review.vo.ActivityHistories;
import codekoi.apiserver.domain.skill.domain.Skill;
import codekoi.apiserver.domain.skill.repository.SkillRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeReviewQuery {

    private final CodeReviewRepository codeReviewRepository;
    private final UserRepository userRepository;
    private final CodeReviewCommentRepository commentRepository;

    private final CodeFavoriteRepository favoriteRepository;
    private final SkillRepository skillRepository;
    private final CodeReviewSkillRepository codeReviewSkillRepository;
    private final LikeRepository likeRepository;

    public List<UserCodeReviewDto> findRequestedCodeReviews(Long sessionUserId, Long userId) {
        final User user = userRepository.findByUserId(userId);
        final List<CodeReview> reviews = codeReviewRepository.findByUser(user);

        final List<Favorite> favorites = favoriteRepository.findFavoriteByUserAndCodeReviewIn(user, reviews);

        return UserCodeReviewDto.listOf(reviews, favorites, sessionUserId.equals(userId));
    }

    public List<UserCodeReviewDto> findFavoriteCodeReviews(Long sessionUserId, Long userId) {
        final User user = userRepository.findByUserId(userId);
        final List<Favorite> favorites = favoriteRepository.findFavoritesByUser(user);

        return UserCodeReviewDto.listOf(favorites, sessionUserId.equals(userId));
    }

    public CodeReviewDetailDto findCodeReviewDetail(Long sessionUserId, Long codeReviewId) {
        final CodeReview codeReview = codeReviewRepository.findByCodeReviewIdWithUser(codeReviewId)
                .orElseThrow(CodeReviewNotFoundException::new);

        final User reviewRequestUser = codeReview.getUser();
        final Optional<Favorite> optionalFavorite = favoriteRepository.findByUser(reviewRequestUser);

        return CodeReviewDetailDto.of(codeReview, optionalFavorite.isPresent(),
                sessionUserId.equals(reviewRequestUser.getId()));
    }

    public List<UserSkillStatistics> findUserSkillStatistics(Long userId) {
        final User user = userRepository.findByUserId(userId);

        final List<CodeReview> reviews = codeReviewRepository.findByUser(user);
        final List<CodeReviewComment> comments = commentRepository.findByUserId(user.getId());

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

    public List<UserActivityHistory> findUserHistory(Long userId) {
        final User user = userRepository.findByUserId(userId);

        final List<CodeReview> codeReviews = codeReviewRepository.findTop10ByUserOrderByCreatedAtDesc(user);
        final List<CodeReviewComment> comments = commentRepository.findTop10ByUserOrderByCreatedAtDesc(user);
        final List<Like> likes = likeRepository.findTop10ByUserOrderByCreatedAtDesc(user);
        final List<Favorite> favorites = favoriteRepository.findTop10ByUserOrderByCreatedAtDesc(user);

        final ActivityHistories histories = new ActivityHistories(codeReviews, comments, likes, favorites);
        final List<Activity> top = histories.getTopN();

        return UserActivityHistory.listFrom(top);
    }

    private List<CodeReviewSkill> getCodeReviewSkills(List<CodeReview> reviews, List<CodeReviewComment> comments) {
        final List<Long> codeReviewIds = Stream.concat(
                        reviews.stream(),
                        comments.stream()
                                .map(CodeReviewComment::getCodeReview)
                ).map(CodeReview::getId)
                .toList();

        return codeReviewSkillRepository.findByCodeReviewIdsIn(codeReviewIds);
    }

    private Map<Skill, Long> getCountMap(List<CodeReview> reviews, List<CodeReviewComment> comments, List<CodeReviewSkill> codeReviewSkills) {
        //key: 코드리뷰 아이디. value: 유저가 남긴 코드리뷰 댓글
        final Map<Long, List<CodeReviewComment>> commentsMapByReviewId = comments.stream()
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

    public List<HotCodeReview> getHotReviews() {
        final List<Long> hotReviewIds = favoriteRepository.hotReviewRank();
        final List<CodeReview> reviews = codeReviewRepository.findAllById(hotReviewIds);

        return HotCodeReview.listFrom(reviews);
    }
}
