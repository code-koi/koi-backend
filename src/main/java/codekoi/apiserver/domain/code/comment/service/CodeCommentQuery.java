package codekoi.apiserver.domain.code.comment.service;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.comment.dto.CodeCommentDetailDto;
import codekoi.apiserver.domain.code.comment.repository.CodeReviewCommentRepository;
import codekoi.apiserver.domain.code.review.domain.CodeReview;
import codekoi.apiserver.domain.code.comment.dto.UserCodeCommentDto;
import codekoi.apiserver.domain.code.review.repository.CodeReviewRepository;
import codekoi.apiserver.domain.koi.history.domain.KoiHistory;
import codekoi.apiserver.domain.koi.history.repository.KoiHistoryRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeCommentQuery {

    private final UserRepository userRepository;
    private final CodeReviewCommentRepository codeReviewCommentRepository;
    private final KoiHistoryRepository koiHistoryRepository;
    private final CodeReviewRepository codeReviewRepository;

    public List<UserCodeCommentDto> getUserComments(Long userId) {
        final User user = userRepository.findByUserId(userId);
        final List<CodeReviewComment> comments = codeReviewCommentRepository.findByUser(user.getId());

        final List<KoiHistory> koiHistories = koiHistoryRepository.findUserCommentKoiHistory(
                comments.stream()
                        .map(CodeReviewComment::getId)
                        .collect(Collectors.toList())
        );

        return UserCodeCommentDto.listOf(user, comments, koiHistories);
    }

    public List<CodeCommentDetailDto> getCommentsOnReview(Long sessionUserId, Long reviewId) {
        final CodeReview codeReview = codeReviewRepository.findByCodeReviewId(reviewId);
        final List<CodeReviewComment> comments = codeReviewCommentRepository.findByCodeReviewId(codeReview.getId());

        final List<Long> commentIds = comments.stream()
                .map(CodeReviewComment::getId)
                .toList();

        final List<KoiHistory> koiHistories = koiHistoryRepository.findUserCommentKoiHistory(commentIds);

        return CodeCommentDetailDto.listOf(comments, koiHistories, sessionUserId);
    }
}
