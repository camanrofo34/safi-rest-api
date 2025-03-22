package unv.upb.safi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unv.upb.safi.domain.dto.request.TeacherRequest;
import unv.upb.safi.domain.dto.response.TeacherResponse;
import unv.upb.safi.service.TeacherService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/teachers")
@Validated
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<TeacherResponse> registerTeacher(@Valid @RequestBody TeacherRequest teacherRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Registering teacher {}", transactionId, teacherRequest.getUsername());
        MDC.put("transactionId", transactionId.toString());

        try {
            TeacherResponse teacherResponse = teacherService.registerTeacher(teacherRequest);
            log.info("Transaction ID: {}, Teacher {} registered successfully", transactionId, teacherRequest.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(teacherResponse);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{teacherId:\\d+}")
    public ResponseEntity<TeacherResponse> updateTeacher(@PathVariable Long teacherId, @Valid @RequestBody TeacherRequest teacherRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating teacher with ID {}", transactionId, teacherId);
        MDC.put("transactionId", transactionId.toString());

        try {
            TeacherResponse teacherResponse = teacherService.updateTeacher(teacherId, teacherRequest);
            log.info("Transaction ID: {}, Teacher with ID {} updated successfully", transactionId, teacherId);
            return ResponseEntity.status(HttpStatus.OK).body(teacherResponse);
        } finally {
            MDC.clear();
        }
    }

    @DeleteMapping("/{teacherId:\\d+}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long teacherId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Deleting teacher with ID {}", transactionId, teacherId);
        MDC.put("transactionId", transactionId.toString());

        try {
            teacherService.deleteTeacher(teacherId);
            log.info("Transaction ID: {}, Teacher with ID {} deleted successfully", transactionId, teacherId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/{teacherId:\\d+}")
    public ResponseEntity<TeacherResponse> getTeacher(@PathVariable Long teacherId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching teacher with ID {}", transactionId, teacherId);
        MDC.put("transactionId", transactionId.toString());

        try {
            TeacherResponse teacherResponse = teacherService.getTeacher(teacherId);
            log.info("Transaction ID: {}, Teacher with ID {} fetched successfully", transactionId, teacherId);
            return ResponseEntity.status(HttpStatus.OK).body(teacherResponse);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<Page<TeacherResponse>> getAllTeachers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching all teachers", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            Page<TeacherResponse> teachers = teacherService.getAllTeachers(page, size, sortBy, direction);
            log.info("Transaction ID: {}, All teachers fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(teachers);
        } finally {
            MDC.clear();
        }
    }
}
