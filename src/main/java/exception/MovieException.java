package exception;

public class MovieException extends RuntimeException {
    public MovieException(Throwable cause) {
        super(cause);
    }

    public MovieException(String message) {
        super(message);
    }
}
