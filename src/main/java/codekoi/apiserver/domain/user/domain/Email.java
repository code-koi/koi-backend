package codekoi.apiserver.domain.user.domain;

import codekoi.apiserver.global.error.exception.DomainLogicException;
import codekoi.apiserver.global.error.exception.ErrorInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

    public static final int MIN_LENGTH = 5;
    public static final int MAX_LENGTH = 80;

    private static final Pattern PATTERN = Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*[.][a-zA-Z]{2,3}$");

    @Column(name = "email")
    private String value;

    public Email(String email) {
        String trimmed = StringUtils.trimWhitespace(email);
        validate(trimmed);
        this.value = trimmed;
    }

    private void validate(String email) {
        if (!StringUtils.hasText(email) ||
                email.length() < MIN_LENGTH || email.length() > MAX_LENGTH ||
                !PATTERN.matcher(email).matches()) {
            throw new DomainLogicException(ErrorInfo.USER_EMAIL_ERROR);
        }
    }
}
