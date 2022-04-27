package socialnetwork.domain.exceptions;

public class MessageValidationException extends RuntimeException{
    public MessageValidationException() {

    }
    public MessageValidationException(String message) {
        super(message);
    }
}
