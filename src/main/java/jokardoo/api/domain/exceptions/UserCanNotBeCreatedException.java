package jokardoo.api.domain.exceptions;

public class UserCanNotBeCreatedException extends RuntimeException {
    public UserCanNotBeCreatedException(String message) {
        super(message);
    }
}
