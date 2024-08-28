package jokardoo.api.domain.exceptions;

public class UserCanNotBeUpdatedException extends RuntimeException {
    public UserCanNotBeUpdatedException(String message) {
        super(message);
    }
}
