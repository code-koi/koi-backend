package codekoi.apiserver.domain.user.controller;

import codekoi.apiserver.domain.user.dto.UserDetail;
import codekoi.apiserver.domain.user.dto.UserToken;
import codekoi.apiserver.domain.user.service.UserQuery;
import codekoi.apiserver.global.token.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserQuery userQuery;

    @GetMapping("/{userId}")
    public UserDetail getUserDetail(@Principal UserToken sessionUser,
                                        @PathVariable Long userId) {
        return userQuery.gerUserDetail(sessionUser.getUserId(), userId);
    }
}
