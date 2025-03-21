package unv.upb.safi.exception.entityNotFoundException;

import jakarta.persistence.EntityNotFoundException;

public class TeacherNotFoundException extends EntityNotFoundException {

    public TeacherNotFoundException(String message) {
        super("Teacher with id '" + message + "' not found");
    }

}
