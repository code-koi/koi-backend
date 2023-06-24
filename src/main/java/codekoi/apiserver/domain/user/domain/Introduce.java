package codekoi.apiserver.domain.user.domain;

import codekoi.apiserver.domain.user.exception.InvalidUserIntroduceException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Introduce {

    public static final int MAX_SIZE = 1024;

    @Column(name = "introduce")
    private String value;

    public Introduce(String introduce) {
        String trimmed = StringUtils.trimWhitespace(introduce);
        validate(trimmed);
        this.value = trimmed;
    }

    private void validate(String introduce) {
        if (!StringUtils.hasText(introduce) ||
                introduce.length() > MAX_SIZE) {
            throw new InvalidUserIntroduceException();
        }
    }
}
