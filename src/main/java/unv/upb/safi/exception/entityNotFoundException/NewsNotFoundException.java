package unv.upb.safi.exception.entityNotFoundException;

import jakarta.persistence.EntityNotFoundException;

public class NewsNotFoundException extends EntityNotFoundException {

    public NewsNotFoundException(String message) {
        super("News with id '" + message + "' not found");
    }
}
