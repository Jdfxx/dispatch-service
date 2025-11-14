package pl.filiphagno.dispatch_service.exception;

public class RetryableException extends RuntimeException {

    public RetryableException(String message) {
        super(message);
    }

    public RetryableException(Exception exception) {
        super(exception);
    }
}
