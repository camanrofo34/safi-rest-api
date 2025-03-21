package unv.upb.safi.exception.entityNotFoundException;

import jakarta.persistence.EntityNotFoundException;

public class WeeklyMenuNotFoundException extends EntityNotFoundException {

    public WeeklyMenuNotFoundException(String message) {
        super("WeeklyMenu with id '" + message + "' not found");
    }
}
