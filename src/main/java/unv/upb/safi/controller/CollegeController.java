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
import unv.upb.safi.domain.dto.request.CollegeRequest;
import unv.upb.safi.domain.dto.response.CollegeResponse;
import unv.upb.safi.service.CollegeService;

import java.util.UUID;

@RestController
@RequestMapping("/college")
@Validated
@Slf4j
public class CollegeController {

    private final CollegeService collegeService;

    @Autowired
    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @PostMapping
    public ResponseEntity<CollegeResponse> createCollege(@RequestBody @Valid CollegeRequest collegeRequest) {
        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());

        log.info("Transaction ID: {}, Adding college {}", transactionId, collegeRequest.getName());

        try {
            CollegeResponse collegeResponse = collegeService.addCollege(collegeRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(collegeResponse);
        } finally {
            MDC.clear();
        }
    }

    @DeleteMapping("/{collegeId:\\d+}")
    public ResponseEntity<Void> deleteCollege(@PathVariable Long collegeId) {
        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());

        log.info("Transaction ID: {}, Deleting college with id {}", transactionId, collegeId);

        try {
            collegeService.deleteCollege(collegeId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/{collegeId:\\d+}")
    public ResponseEntity<CollegeResponse> getCollege(@PathVariable Long collegeId) {
        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());

        log.info("Transaction ID: {}, Fetching college with id {}", transactionId, collegeId);

        try {
            return ResponseEntity.status(HttpStatus.OK).body(collegeService.getCollege(collegeId));
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<Page<CollegeResponse>> getColleges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "collegeId") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());

        log.info("Transaction ID: {}, Fetching all colleges", transactionId);

        try {
            return ResponseEntity.status(HttpStatus.OK).body(collegeService.getColleges(page, size, sortBy, direction));
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CollegeResponse>> getCollegesByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "collegeId") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());

        log.info("Transaction ID: {}, Searching colleges by name '{}'", transactionId, name);

        try {
            return ResponseEntity.status(HttpStatus.OK).body(collegeService.getCollegesByName(name, page, size, sortBy, direction));
        } finally {
            MDC.clear();
        }
    }
}
