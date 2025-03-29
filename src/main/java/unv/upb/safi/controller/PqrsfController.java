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
import unv.upb.safi.domain.dto.request.PqrsfRequest;
import unv.upb.safi.domain.dto.response.PqrsfResponse;
import unv.upb.safi.service.PqrsfService;
import unv.upb.safi.service.impl.PqrsfServiceImpl;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/pqrsf")
@Validated
public class PqrsfController {

    private final PqrsfService pqrsfService;

    @Autowired
    public PqrsfController(PqrsfServiceImpl pqrsfService) {
        this.pqrsfService = pqrsfService;
    }

    @PostMapping("/student/{studentId:\\d+}")
    public ResponseEntity<EntityModel<PqrsfResponse>> createPqrsf(@PathVariable Long studentId, @Valid @RequestBody PqrsfRequest pqrsfRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Creating PQRSF for student {}", transactionId, studentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<PqrsfResponse> response = pqrsfService.createPqrsf(studentId, pqrsfRequest);
            log.info("Transaction ID: {}, PQRSF created successfully for student {}", transactionId, studentId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            MDC.clear();
        }
    }

    @DeleteMapping("/{requestId:\\d+}")
    public ResponseEntity<Void> deletePqrsf(@PathVariable Long requestId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Deleting PQRSF with ID {}", transactionId, requestId);
        MDC.put("transactionId", transactionId.toString());

        try {
            pqrsfService.deletePqrsf(requestId);
            log.info("Transaction ID: {}, PQRSF with ID {} deleted successfully", transactionId, requestId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/{requestId:\\d+}")
    public ResponseEntity<EntityModel<PqrsfResponse>> getPqrsf(@PathVariable Long requestId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching PQRSF with ID {}", transactionId, requestId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<PqrsfResponse> response = pqrsfService.getPqrsf(requestId);
            log.info("Transaction ID: {}, PQRSF with ID {} fetched successfully", transactionId, requestId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PqrsfResponse>>> getAllPqrsf(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching all PQRSFs", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<PqrsfResponse>> response = pqrsfService.getAllPqrsf(pageable);
            log.info("Transaction ID: {}, All PQRSFs fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }
}