package unv.upb.safi.exception.entityNotFoundException;

import jakarta.persistence.EntityNotFoundException;

public class PqrsfNotFoundException extends EntityNotFoundException {

    public PqrsfNotFoundException(String message) {
        super("Pqrsf with id '" + message + "' not found");
    }
}
