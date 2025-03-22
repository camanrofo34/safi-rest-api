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
import unv.upb.safi.domain.dto.request.DependencyRequest;
import unv.upb.safi.domain.dto.response.DependencyResponse;
import unv.upb.safi.service.DependencyService;
import unv.upb.safi.service.impl.DependencyServiceImpl;

import java.util.UUID;

@Validated
@Slf4j
@RestController
@RequestMapping("/dependency")
public class DependencyController {

    private final DependencyService dependencyService;

    @Autowired
    public DependencyController(DependencyServiceImpl dependencyService) {
        this.dependencyService = dependencyService;
    }

    @PostMapping
    public ResponseEntity<DependencyResponse> addDependency(@Valid @RequestBody DependencyRequest dependencyRequest) {
        UUID transactionId = UUID.randomUUID();

        log.info("Transaction ID: {}, Adding dependency {}", transactionId, dependencyRequest.getDependencyName());
        MDC.put("transactionId", transactionId.toString());

        try{
            DependencyResponse response = dependencyService.createDependency(dependencyRequest);
            log.info("Transaction ID: {}, Dependency added successfully", transactionId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            MDC.remove("transactionId");
        }
    }

    @DeleteMapping("/{dependencyId:\\d+}")
    public ResponseEntity<Void> deleteDependency(@PathVariable Long dependencyId) {
        UUID transactionId = UUID.randomUUID();

        log.info("Transaction ID: {}, Deleting dependency with id {}", transactionId, dependencyId);
        MDC.put("transactionId", transactionId.toString());

        try{
            dependencyService.deleteDependency(dependencyId);
            log.info("Transaction ID: {}, Dependency deleted successfully", transactionId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.remove("transactionId");
        }
    }

    @PutMapping("/{dependencyId:\\d+}")
    public ResponseEntity<DependencyResponse> updateDependency(@PathVariable Long dependencyId, @Valid @RequestBody DependencyRequest dependencyRequest) {
        UUID transactionId = UUID.randomUUID();

        log.info("Transaction ID: {}, Updating dependency with id {}", transactionId, dependencyId);
        MDC.put("transactionId", transactionId.toString());

        try {
            DependencyResponse response = dependencyService.updateDependency(dependencyId, dependencyRequest);
            log.info("Transaction ID: {}, Dependency updated successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.remove("transactionId");
        }
    }

    @GetMapping("/{dependencyId:\\d+}")
    public ResponseEntity<DependencyResponse> getDependency(@PathVariable Long dependencyId) {
        UUID transactionId = UUID.randomUUID();

        log.info("Transaction ID: {}, Getting dependency with id {}", transactionId, dependencyId);
        MDC.put("transactionId", transactionId.toString());

        try {
            DependencyResponse response = dependencyService.getDependencyById(dependencyId);
            log.info("Transaction ID: {}, Dependency retrieved successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.remove("transactionId");
        }
    }

    @GetMapping("/component/{componentId:\\d+}")
    public ResponseEntity<Page<DependencyResponse>> getDependenciesByComponentId(@PathVariable Long componentId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "dependencyId") String sortBy,
                                                             @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        UUID transactionId = UUID.randomUUID();

        log.info("Transaction ID: {}, Getting dependencies for component with id {}", transactionId, componentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            Page<DependencyResponse> response = dependencyService.getDependenciesByComponentId(componentId, page, size, sortBy, direction);
            log.info("Transaction ID: {}, Dependencies retrieved successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.remove("transactionId");
        }
    }
}
