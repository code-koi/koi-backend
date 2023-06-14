package codekoi.apiserver.global.error.exception;

public class DomainLogicException extends InvalidValueException {
    public DomainLogicException(String message) {
        super(message);
    }

    public DomainLogicException(ErrorInfo errorInfo) {
        super(errorInfo);
    }
}
