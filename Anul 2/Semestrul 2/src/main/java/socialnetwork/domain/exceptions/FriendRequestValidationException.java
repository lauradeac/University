package socialnetwork.domain.exceptions;

public class FriendRequestValidationException extends RuntimeException{
    public FriendRequestValidationException() {
    }

    public FriendRequestValidationException(String message) {
        super(message);
    }
}
