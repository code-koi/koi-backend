package codekoi.apiserver.domain.user.service;

import codekoi.apiserver.domain.code.comment.repository.CodeReviewCommentRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.dto.UserDetail;
import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.domain.user.exception.UserNotFoundException;
import codekoi.apiserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserQuery {
    private final UserRepository userRepository;
    private final CodeReviewCommentRepository codeReviewCommentRepository;

    public UserToken getUserAuth(String email) {
        final User user = userRepository.findByEmailValue(email)
                .orElseThrow(() -> {
                    throw new UserNotFoundException();
                });

        return UserToken.from(user);
    }

    public UserDetail gerUserDetail(Long sessionUserId, Long userId) {
        final User user = userRepository.findByUserId(userId);
        final int reviewCount = codeReviewCommentRepository.countByUserId(userId);

        return UserDetail.of(user, reviewCount, sessionUserId);
    }
}
