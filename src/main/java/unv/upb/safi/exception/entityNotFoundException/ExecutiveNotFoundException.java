package unv.upb.safi.exception.entityNotFoundException;

import jakarta.persistence.EntityNotFoundException;

public class ExecutiveNotFoundException extends EntityNotFoundException {

    public ExecutiveNotFoundException(String message) {
        super("Executive with id '" + message + "' not found");
    }
}
