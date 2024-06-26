package greencity.exception.exceptions;

/**
 * Exception that we get when user trying to update their name.
 *
 */
public class UserNameCanNotBeChangedException extends RuntimeException {
    public UserNameCanNotBeChangedException(String message) {
        super(message);
    }
}
