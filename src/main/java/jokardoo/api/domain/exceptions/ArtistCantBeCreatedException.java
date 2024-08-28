package jokardoo.api.domain.exceptions;

public class ArtistCantBeCreatedException extends RuntimeException {
    public ArtistCantBeCreatedException(String message) {
        super(message);
    }
}
