package codekoi.apiserver.global.error.exception;

import lombok.Getter;

public class DomainLogicException extends InvalidValueException {
    public DomainLogicException(String message) {
        super(message);
    }

    public DomainLogicException(ErrorInfo errorInfo) {
        super(errorInfo);
    }
}
