package greencity.exception.exceptions;

/**
 * Exception that we get when user trying to sign-up with name that already
 * registered.
 *
 */
public class UserAlreadyExistByNameException extends RuntimeException {
    public UserAlreadyExistByNameException(String message) {
        super(message);
    }
}