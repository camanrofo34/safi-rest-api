package unv.upb.safi.exception.entityNotFoundException;

import jakarta.persistence.EntityNotFoundException;

public class TagNotFoundException extends EntityNotFoundException {
    public TagNotFoundException(String message) {
        super("Tag with id '" + message + "' not found");
    }
}
