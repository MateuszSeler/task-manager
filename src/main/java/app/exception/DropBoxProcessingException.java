package app.exception;

public class DropBoxProcessingException extends RuntimeException {
    public DropBoxProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DropBoxProcessingException(String message) {
        super(message);
    }
}
