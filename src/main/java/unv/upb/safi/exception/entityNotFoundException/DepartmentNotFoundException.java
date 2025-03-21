package unv.upb.safi.exception.entityNotFoundException;

import jakarta.persistence.EntityNotFoundException;

public class DepartmentNotFoundException extends EntityNotFoundException {

    public DepartmentNotFoundException(String message) {
        super("Department with id '" + message + "' not found");
    }
}
