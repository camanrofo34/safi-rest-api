package unv.upb.safi.exception.entityNotFoundException;

import jakarta.persistence.EntityNotFoundException;

public class FacultyNotFoundException extends EntityNotFoundException {

    public FacultyNotFoundException(String message) {
        super("Faculty with id '" + message + "' not found");
    }
}
