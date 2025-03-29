package unv.upb.safi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unv.upb.safi.domain.dto.request.TeacherRequest;
import unv.upb.safi.domain.dto.response.TeacherResponse;
import unv.upb.safi.service.TeacherService;
import unv.upb.safi.service.impl.TeacherServiceImpl;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/teachers")
@Validated
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherServiceImpl teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<TeacherResponse>> registerTeacher(@Valid @RequestBody TeacherRequest teacherRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Registering teacher {}", transactionId, teacherRequest.getUsername());
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<TeacherResponse> response = teacherService.registerTeacher(teacherRequest);
            log.info("Transaction ID: {}, Teacher {} registered successfully", transactionId, teacherRequest.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{teacherId:\\d+}")
    public ResponseEntity<EntityModel<TeacherResponse>> updateTeacher(@PathVariable Long teacherId, @Valid @RequestBody TeacherRequest teacherRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating teacher with ID {}", transactionId, teacherId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<TeacherResponse> response = teacherService.updateTeacher(teacherId, teacherRequest);
            log.info("Transaction ID: {}, Teacher with ID {} updated successfully", transactionId, teacherId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
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
    public ResponseEntity<EntityModel<TeacherResponse>> getTeacher(@PathVariable Long teacherId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching teacher with ID {}", transactionId, teacherId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<TeacherResponse> response = teacherService.getTeacher(teacherId);
            log.info("Transaction ID: {}, Teacher with ID {} fetched successfully", transactionId, teacherId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<TeacherResponse>>> getAllTeachers(
            @PageableDefault(size = 10, sort = "teacherId") Pageable pageable) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching all teachers", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<TeacherResponse>> response = teacherService.getAllTeachers(pageable);
            log.info("Transaction ID: {}, All teachers fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<TeacherResponse>>> getTeachersByName(
            @RequestParam String name,
            @PageableDefault(size = 10, sort = "teacherId") Pageable pageable) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Searching teachers by name '{}'", transactionId, name);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<TeacherResponse>> response = teacherService.getTeachersByName(name, pageable);
            log.info("Transaction ID: {}, Teachers retrieved by name", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }
}