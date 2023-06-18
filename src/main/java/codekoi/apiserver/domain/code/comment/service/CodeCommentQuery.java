package codekoi.apiserver.domain.code.comment.service;

import codekoi.apiserver.domain.code.comment.domain.CodeReviewComment;
import codekoi.apiserver.domain.code.comment.repository.CodeReviewCommentRepository;
import codekoi.apiserver.domain.code.review.dto.UserCodeCommentDto;
import codekoi.apiserver.domain.koi.domain.KoiType;
import codekoi.apiserver.domain.koi.history.domain.KoiHistory;
import codekoi.apiserver.domain.koi.history.repository.KoiHistoryRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeCommentQuery {

    private final UserRepository userRepository;
    private final CodeReviewCommentRepository codeReviewCommentRepository;
    private final KoiHistoryRepository koiHistoryRepository;

    public List<UserCodeCommentDto> getUserComments(Long userId) {
        final User user = userRepository.findUserById(userId);
        final List<CodeReviewComment> comments = codeReviewCommentRepository.findByUser(user.getId());

        final List<KoiHistory> koiHistories = koiHistoryRepository.findUserCommentKoiHistory(
                comments.stream()
                        .map(CodeReviewComment::getId)
                        .collect(Collectors.toList())
        );

        final Map<Long, KoiType> koiMap = koiHistories.stream()
                .collect(Collectors.toMap(koiHistory -> koiHistory.getCodeReviewComment().getId(),
                        KoiHistory::getKoiType));

        return comments.stream()
                .map(c -> UserCodeCommentDto.of(user, c, koiMap.get(c.getId())))
                .collect(Collectors.toList());
    }
}
