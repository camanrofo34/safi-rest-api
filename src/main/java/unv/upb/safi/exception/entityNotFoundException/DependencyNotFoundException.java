package unv.upb.safi.exception.entityNotFoundException;

import jakarta.persistence.EntityNotFoundException;

public class DependencyNotFoundException extends EntityNotFoundException {

    public DependencyNotFoundException(String message) {
        super("Dependency with id " + message + " not found");
    }
}
