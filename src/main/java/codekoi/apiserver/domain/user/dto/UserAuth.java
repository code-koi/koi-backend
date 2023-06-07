package codekoi.apiserver.domain.user.dto;

import codekoi.apiserver.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

public class UserAuth {
    private Long userId;

    public UserAuth(Long userId) {
        this.userId = userId;
    }

    public static UserAuth from(User user) {
        return new UserAuth(user.getId());
    }
}
