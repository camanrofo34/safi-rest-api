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
import unv.upb.safi.domain.dto.request.FacultyRequest;
import unv.upb.safi.domain.dto.response.FacultyResponse;
import unv.upb.safi.service.FacultyService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/faculty")
@Validated
public class FacultyController {

    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<FacultyResponse> addFaculty(@Valid @RequestBody FacultyRequest facultyRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Adding faculty {}", transactionId, facultyRequest.getFacultyName());
        MDC.put("transactionId", transactionId.toString());

        try {
            FacultyResponse facultyResponse = facultyService.addFaculty(facultyRequest);
            log.info("Transaction ID: {}, Faculty {} added successfully", transactionId, facultyRequest.getFacultyName());
            return ResponseEntity.status(HttpStatus.CREATED).body(facultyResponse);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{facultyId:\\d+}")
    public ResponseEntity<FacultyResponse> updateFaculty(@PathVariable Long facultyId, @Valid @RequestBody FacultyRequest facultyRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating faculty with ID {}", transactionId, facultyId);
        MDC.put("transactionId", transactionId.toString());

        try {
            FacultyResponse facultyResponse = facultyService.updateFaculty(facultyId, facultyRequest);
            log.info("Transaction ID: {}, Faculty with ID {} updated successfully", transactionId, facultyId);
            return ResponseEntity.status(HttpStatus.OK).body(facultyResponse);
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
    public ResponseEntity<FacultyResponse> getFaculty(@PathVariable Long facultyId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching faculty with ID {}", transactionId, facultyId);
        MDC.put("transactionId", transactionId.toString());

        try {
            FacultyResponse facultyResponse = facultyService.getFaculty(facultyId);
            log.info("Transaction ID: {}, Faculty with ID {} fetched successfully", transactionId, facultyId);
            return ResponseEntity.status(HttpStatus.OK).body(facultyResponse);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/college/{collegeId:\\d+}")
    public ResponseEntity<Page<FacultyResponse>> getFacultyByCollegeId(
            @PathVariable Long collegeId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching faculties by college ID {}", transactionId, collegeId);
        MDC.put("transactionId", transactionId.toString());

        try {
            Page<FacultyResponse> faculties = facultyService.getFacultyByCollegeId(collegeId, page, size, sortBy, direction);
            log.info("Transaction ID: {}, Faculties by college ID {} fetched successfully", transactionId, collegeId);
            return ResponseEntity.status(HttpStatus.OK).body(faculties);
        } finally {
            MDC.clear();
        }
    }
}