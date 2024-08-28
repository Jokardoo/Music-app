package jokardoo.api.domain.exceptions;

public class UserCannotBeDeletedException extends RuntimeException {
    public UserCannotBeDeletedException(String message) {
        super(message);
    }
}
