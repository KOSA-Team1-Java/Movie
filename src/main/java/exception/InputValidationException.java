package exception;

public class InputValidationException extends CustomException {
    public InputValidationException(Throwable cause) {
        super(cause);
    }

    public InputValidationException(String message) {
        super(message);
    }

    public InputValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
