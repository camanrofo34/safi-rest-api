package unv.upb.safi.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import unv.upb.safi.exception.AppointmentConflictException;
import unv.upb.safi.exception.entityNotFoundException.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<String> handleAppointmentNotFoundException(AppointmentNotFoundException e) {
        log.error("Transaction Id: {}, Appointment not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(CollegeNotFoundException.class)
    public ResponseEntity<String> handleCollegeNotFoundException(CollegeNotFoundException e) {
        log.error("Transaction Id: {}, College not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ComponentNotFoundException.class)
    public ResponseEntity<String> handleComponentNotFoundException(ComponentNotFoundException e) {
        log.error("Transaction Id: {}, Component not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<String> handleDepartmentNotFoundException(DepartmentNotFoundException e) {
        log.error("Transaction Id: {}, Department not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DependencyNotFoundException.class)
    public ResponseEntity<String> handleDependencyNotFoundException(DependencyNotFoundException e) {
        log.error("Transaction Id: {}, Dependency not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(FacultyNotFoundException.class)
    public ResponseEntity<String> handleFacultyNotFoundException(FacultyNotFoundException e) {
        log.error("Transaction Id: {}, Faculty not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(NewsNotFoundException.class)
    public ResponseEntity<String> handleNewsNotFoundException(NewsNotFoundException e) {
        log.error("Transaction Id: {}, News not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(PqrsfNotFoundException.class)
    public ResponseEntity<String> handlePqrsfNotFoundException(PqrsfNotFoundException e) {
        log.error("Transaction Id: {}, Pqrsf not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleStudentNotFoundException(StudentNotFoundException e) {
        log.error("Transaction Id: {}, Student not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<String> handleTagNotFoundException(TagNotFoundException e) {
        log.error("Transaction Id: {}, Tag not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error("Transaction Id: {}, Username not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(TeacherNotFoundException.class)
    public ResponseEntity<String> handleTeacherNotFoundException(TeacherNotFoundException e) {
        log.error("Transaction Id: {}, Teacher not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(WeeklyMenuNotFoundException.class)
    public ResponseEntity<String> handleWeeklyMenuNotFoundException(WeeklyMenuNotFoundException e) {
        log.error("Transaction Id: {}, WeeklyMenu not found. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(AppointmentConflictException.class)
    public ResponseEntity<String> handleAppointmentConflictException(AppointmentConflictException e) {
        log.error("Transaction Id: {}, Appointment conflict. Error: {}", MDC.get("transactionId"), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
