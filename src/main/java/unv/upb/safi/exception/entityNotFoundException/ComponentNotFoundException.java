package unv.upb.safi.exception.entityNotFoundException;

import jakarta.persistence.EntityNotFoundException;

public class ComponentNotFoundException extends EntityNotFoundException {

    public ComponentNotFoundException(String message) {
        super("Component with id '" + message + "' not found");
    }
}
