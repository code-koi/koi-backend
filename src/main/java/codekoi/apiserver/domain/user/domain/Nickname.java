package codekoi.apiserver.domain.user.domain;

import codekoi.apiserver.global.error.exception.DomainLogicException;
import codekoi.apiserver.global.error.exception.ErrorInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Embeddable
@Getter
@NoArgsConstructor
public class Nickname {

    private static final int MIN_SIZE = 3;
    private static final int MAX_SIZE = 10;

    @Column(name = "nickname")
    private String value;

    public Nickname(String nickname) {
        final String trimmed = StringUtils.trimWhitespace(nickname);
        validate(trimmed);
        this.value = trimmed;
    }

    private void validate(String nickname) {
        if (!StringUtils.hasText(nickname) ||
                nickname.length() < MIN_SIZE ||
                nickname.length() > MAX_SIZE) {
            throw new DomainLogicException(ErrorInfo.USER_NICKNAME_OVER);
        }
    }
}
