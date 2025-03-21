package unv.upb.safi.exception.entityNotFoundException;

import jakarta.persistence.EntityNotFoundException;

public class StudentNotFoundException extends EntityNotFoundException {

    public StudentNotFoundException(String message) {
        super("Student with id '" + message + "' not found");
    }
}
