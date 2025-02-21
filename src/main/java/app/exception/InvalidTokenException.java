package app.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message, Exception exception) {
        super(message);
    }
}
