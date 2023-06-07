package codekoi.apiserver.domain.user.dto;

import codekoi.apiserver.domain.user.domain.User;
import lombok.Getter;

@Getter
public class UserToken {
    private Long userId;

    public UserToken(Long userId) {
        this.userId = userId;
    }

    public static UserToken from(User user) {
        return new UserToken(user.getId());
    }
}
