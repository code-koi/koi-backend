package codekoi.apiserver.domain.user.service;

import codekoi.apiserver.domain.code.comment.repository.CodeReviewCommentRepository;
import codekoi.apiserver.domain.user.domain.User;
import codekoi.apiserver.domain.user.dto.UserDetail;
import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.domain.user.repository.UserRepository;
import codekoi.apiserver.global.error.exception.ErrorInfo;
import codekoi.apiserver.global.error.exception.InvalidValueException;
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
                    throw new InvalidValueException(ErrorInfo.USER_NOT_FOUND_ERROR);
                });

        return UserToken.from(user);
    }

    public UserDetail gerUserDetail(Long sessionUserId, Long userId) {
        final User user = userRepository.findUserById(userId);
        final int reviewCount = codeReviewCommentRepository.countByUserId(userId);

        return UserDetail.of(user, reviewCount, sessionUserId);
    }
}
