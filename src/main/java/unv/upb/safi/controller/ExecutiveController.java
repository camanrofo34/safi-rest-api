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
import unv.upb.safi.domain.dto.request.ExecutiveRequest;
import unv.upb.safi.domain.dto.response.ExecutiveResponse;
import unv.upb.safi.service.ExecutiveService;
import unv.upb.safi.service.impl.ExecutiveServiceImpl;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/executive")
@Validated
public class ExecutiveController {

    private final ExecutiveService executiveService;

    @Autowired
    public ExecutiveController(ExecutiveServiceImpl executiveService) {
        this.executiveService = executiveService;
    }

    @PostMapping
    public ResponseEntity<ExecutiveResponse> registerExecutive (@Valid @RequestBody ExecutiveRequest executiveRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Adding executive {}", transactionId, executiveRequest.getFirstName());
        MDC.put("transactionId", transactionId.toString());

        try {
            ExecutiveResponse executiveResponse = executiveService.createExecutive(executiveRequest);
            log.info("Transaction ID: {}, Executive {} added successfully", transactionId, executiveRequest.getFirstName());
            return ResponseEntity.status(HttpStatus.CREATED).body(executiveResponse);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{executiveId:\\d+}")
    public ResponseEntity<ExecutiveResponse> updateExecutive(@PathVariable Long executiveId, @Valid @RequestBody ExecutiveRequest executiveRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating executive {}", transactionId, executiveRequest.getFirstName());
        MDC.put("transactionId", transactionId.toString());

        try {
            ExecutiveResponse executiveResponse = executiveService.updateExecutive(executiveId, executiveRequest);
            log.info("Transaction ID: {}, Executive {} updated successfully", transactionId, executiveRequest.getFirstName());
            return ResponseEntity.status(HttpStatus.OK).body(executiveResponse);
        } finally {
            MDC.clear();
        }
    }

    @DeleteMapping("/{executiveId:\\d+}")
    public ResponseEntity<Void> deleteExecutive(@PathVariable Long executiveId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Deleting executive with ID {}", transactionId, executiveId);
        MDC.put("transactionId", transactionId.toString());

        try {
            executiveService.deleteExecutive(executiveId);
            log.info("Transaction ID: {}, Executive with ID {} deleted successfully", transactionId, executiveId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/{executiveId:\\d+}")
    public ResponseEntity<ExecutiveResponse> getExecutive(@PathVariable Long executiveId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching executive with ID {}", transactionId, executiveId);
        MDC.put("transactionId", transactionId.toString());

        try {
            ExecutiveResponse executiveResponse = executiveService.getExecutive(executiveId);
            log.info("Transaction ID: {}, Executive with ID {} fetched successfully", transactionId, executiveId);
            return ResponseEntity.status(HttpStatus.OK).body(executiveResponse);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<Page<ExecutiveResponse>> getExecutives(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
            ) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching executives", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            Page<ExecutiveResponse> executives = executiveService.getExecutives(page, size, sortBy, direction);
            log.info("Transaction ID: {}, Executives fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(executives);
        } finally {
            MDC.clear();
        }
    }
}
