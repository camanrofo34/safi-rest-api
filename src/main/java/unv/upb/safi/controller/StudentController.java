package unv.upb.safi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unv.upb.safi.domain.dto.request.StudentRequest;
import unv.upb.safi.domain.dto.response.StudentResponse;
import unv.upb.safi.service.StudentService;
import unv.upb.safi.service.impl.StudentServiceImpl;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/students")
@Validated
public class StudentController {

    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentController(StudentServiceImpl studentService, PasswordEncoder passwordEncoder) {
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<EntityModel<StudentResponse>> registerStudent(@Valid @RequestBody StudentRequest studentRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Registering student {}", transactionId, studentRequest.getUsername());
        MDC.put("transactionId", transactionId.toString());

        try {
            // Encode the password before saving
            studentRequest.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
            // Call the service to register the student
            EntityModel<StudentResponse> response = studentService.registerStudent(studentRequest);
            log.info("Transaction ID: {}, Student {} registered successfully", transactionId, studentRequest.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{studentId:\\d+}")
    public ResponseEntity<EntityModel<StudentResponse>> updateStudent(@PathVariable Long studentId, @Valid @RequestBody StudentRequest studentRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating student with ID {}", transactionId, studentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<StudentResponse> response = studentService.updateStudent(studentId, studentRequest);
            log.info("Transaction ID: {}, Student with ID {} updated successfully", transactionId, studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @DeleteMapping("/{studentId:\\d+}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Deleting student with ID {}", transactionId, studentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            studentService.deleteStudent(studentId);
            log.info("Transaction ID: {}, Student with ID {} deleted successfully", transactionId, studentId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/{studentId:\\d+}")
    public ResponseEntity<EntityModel<StudentResponse>> getStudent(@PathVariable Long studentId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching student with ID {}", transactionId, studentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<StudentResponse> response = studentService.getStudent(studentId);
            log.info("Transaction ID: {}, Student with ID {} fetched successfully", transactionId, studentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }
}