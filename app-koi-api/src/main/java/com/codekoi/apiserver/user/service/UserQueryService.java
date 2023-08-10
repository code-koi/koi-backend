package com.codekoi.apiserver.user.service;

import com.codekoi.apiserver.comment.repository.ReviewCommentQueryRepository;
import com.codekoi.apiserver.user.dto.UserDetail;
import com.codekoi.domain.user.User;
import com.codekoi.domain.user.UserRepository;
import com.codekoi.domain.user.usecase.QueryUserByEmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService {

    private final ReviewCommentQueryRepository reviewCommentQueryRepository;

    private final QueryUserByEmailUseCase queryUserByEmailUseCase;
    private final UserRepository userRepository;

    public Long getUserIdByEmail(String email) {
        final User user = queryUserByEmailUseCase.query(new QueryUserByEmailUseCase.Query(email));
        return user.getId();
    }

    public UserDetail getUserDetail(Long sessionUserId, Long userId) {
        final User user = userRepository.getOneById(userId);
        final int reviewCount = reviewCommentQueryRepository.countByUserId(userId);

        return UserDetail.of(user, reviewCount, sessionUserId);
    }
}
