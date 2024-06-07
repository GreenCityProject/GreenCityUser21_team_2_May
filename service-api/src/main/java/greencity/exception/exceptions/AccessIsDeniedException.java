package greencity.exception.exceptions;

import lombok.Getter;

@Getter
public class AccessIsDeniedException extends RuntimeException {

    private final String googleToken;

    public AccessIsDeniedException(String message, String userId) {
        super(message);
        this.googleToken = userId;
    }

}
