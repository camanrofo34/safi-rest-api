package unv.upb.safi.exception.entityNotFoundException;

import jakarta.persistence.EntityNotFoundException;

public class CollegeNotFoundException extends EntityNotFoundException {

    public CollegeNotFoundException(String message) {
        super("College with id '" + message + "' not found");
    }
}
