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
import unv.upb.safi.domain.dto.request.FacultyRequest;
import unv.upb.safi.domain.dto.response.FacultyResponse;
import unv.upb.safi.service.FacultyService;
import unv.upb.safi.service.impl.FacultyServiceImpl;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/faculties")
@Validated
public class FacultyController {

    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyServiceImpl facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<FacultyResponse>> addFaculty(@Valid @RequestBody FacultyRequest facultyRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Adding faculty {}", transactionId, facultyRequest.getFacultyName());
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<FacultyResponse> response = facultyService.addFaculty(facultyRequest);
            log.info("Transaction ID: {}, Faculty {} added successfully", transactionId, facultyRequest.getFacultyName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{facultyId:\\d+}")
    public ResponseEntity<EntityModel<FacultyResponse>> updateFaculty(@PathVariable Long facultyId, @Valid @RequestBody FacultyRequest facultyRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating faculty with ID {}", transactionId, facultyId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<FacultyResponse> response = facultyService.updateFaculty(facultyId, facultyRequest);
            log.info("Transaction ID: {}, Faculty with ID {} updated successfully", transactionId, facultyId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @DeleteMapping("/{facultyId:\\d+}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long facultyId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Deleting faculty with ID {}", transactionId, facultyId);
        MDC.put("transactionId", transactionId.toString());

        try {
            facultyService.deleteFaculty(facultyId);
            log.info("Transaction ID: {}, Faculty with ID {} deleted successfully", transactionId, facultyId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/{facultyId:\\d+}")
    public ResponseEntity<EntityModel<FacultyResponse>> getFaculty(@PathVariable Long facultyId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching faculty with ID {}", transactionId, facultyId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<FacultyResponse> response = facultyService.getFaculty(facultyId);
            log.info("Transaction ID: {}, Faculty with ID {} fetched successfully", transactionId, facultyId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/college/{collegeId:\\d+}")
    public ResponseEntity<PagedModel<EntityModel<FacultyResponse>>> getFacultyByCollegeId(
            @PathVariable Long collegeId,
            @PageableDefault(size = 10, sort = "facultyId") Pageable pageable) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching faculties by college ID {}", transactionId, collegeId);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<FacultyResponse>> response = facultyService.getFacultiesByCollegeId(collegeId, pageable);
            log.info("Transaction ID: {}, Faculties by college ID {} fetched successfully", transactionId, collegeId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<FacultyResponse>>> getFacultiesByName(
            @RequestParam String name,
            @PageableDefault(size = 10, sort = "facultyId") Pageable pageable) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Searching faculties by name '{}'", transactionId, name);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<FacultyResponse>> response = facultyService.getFacultiesByName(name, pageable);
            log.info("Transaction ID: {}, Faculties retrieved by name", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }
}