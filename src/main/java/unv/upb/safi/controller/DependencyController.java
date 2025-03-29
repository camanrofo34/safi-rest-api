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
    import unv.upb.safi.domain.dto.request.DependencyRequest;
    import unv.upb.safi.domain.dto.response.DependencyResponse;
    import unv.upb.safi.service.DependencyService;
    import unv.upb.safi.service.impl.DependencyServiceImpl;

    import java.util.UUID;

    @Validated
    @Slf4j
    @RestController
    @RequestMapping("/dependencies")
    public class DependencyController {

        private final DependencyService dependencyService;

        @Autowired
        public DependencyController(DependencyServiceImpl dependencyService) {
            this.dependencyService = dependencyService;
        }

        @PostMapping
        public ResponseEntity<EntityModel<DependencyResponse>> addDependency(@Valid @RequestBody DependencyRequest dependencyRequest) {
            UUID transactionId = UUID.randomUUID();
            log.info("Transaction ID: {}, Adding dependency {}", transactionId, dependencyRequest.getDependencyName());
            MDC.put("transactionId", transactionId.toString());

            try {
                EntityModel<DependencyResponse> response = dependencyService.createDependency(dependencyRequest);
                log.info("Transaction ID: {}, Dependency added successfully", transactionId);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } finally {
                MDC.clear();
            }
        }

        @DeleteMapping("/{dependencyId:\\d+}")
        public ResponseEntity<Void> deleteDependency(@PathVariable Long dependencyId) {
            UUID transactionId = UUID.randomUUID();
            log.info("Transaction ID: {}, Deleting dependency with id {}", transactionId, dependencyId);
            MDC.put("transactionId", transactionId.toString());

            try {
                dependencyService.deleteDependency(dependencyId);
                log.info("Transaction ID: {}, Dependency deleted successfully", transactionId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } finally {
                MDC.clear();
            }
        }

        @PutMapping("/{dependencyId:\\d+}")
        public ResponseEntity<EntityModel<DependencyResponse>> updateDependency(@PathVariable Long dependencyId, @Valid @RequestBody DependencyRequest dependencyRequest) {
            UUID transactionId = UUID.randomUUID();
            log.info("Transaction ID: {}, Updating dependency with id {}", transactionId, dependencyId);
            MDC.put("transactionId", transactionId.toString());

            try {
                EntityModel<DependencyResponse> response = dependencyService.updateDependency(dependencyId, dependencyRequest);
                log.info("Transaction ID: {}, Dependency updated successfully", transactionId);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } finally {
                MDC.clear();
            }
        }

        @GetMapping("/{dependencyId:\\d+}")
        public ResponseEntity<EntityModel<DependencyResponse>> getDependency(@PathVariable Long dependencyId) {
            UUID transactionId = UUID.randomUUID();
            log.info("Transaction ID: {}, Getting dependency with id {}", transactionId, dependencyId);
            MDC.put("transactionId", transactionId.toString());

            try {
                EntityModel<DependencyResponse> response = dependencyService.getDependencyById(dependencyId);
                log.info("Transaction ID: {}, Dependency retrieved successfully", transactionId);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } finally {
                MDC.clear();
            }
        }

        @GetMapping("/component/{componentId:\\d+}")
        public ResponseEntity<PagedModel<EntityModel<DependencyResponse>>> getDependenciesByComponentId(
                @PathVariable Long componentId,
                @PageableDefault(size = 10, sort = "dependencyId") Pageable pageable) {
            UUID transactionId = UUID.randomUUID();
            log.info("Transaction ID: {}, Getting dependencies for component with id {}", transactionId, componentId);
            MDC.put("transactionId", transactionId.toString());

            try {
                PagedModel<EntityModel<DependencyResponse>> response = dependencyService.getDependenciesByComponentId(componentId, pageable);
                log.info("Transaction ID: {}, Dependencies retrieved successfully", transactionId);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } finally {
                MDC.clear();
            }
        }

        @GetMapping("/search")
        public ResponseEntity<PagedModel<EntityModel<DependencyResponse>>> getDependenciesByName(
                @RequestParam String name,
                @PageableDefault(size = 10, sort = "dependencyId") Pageable pageable) {
            UUID transactionId = UUID.randomUUID();
            log.info("Transaction ID: {}, Searching dependencies by name '{}'", transactionId, name);
            MDC.put("transactionId", transactionId.toString());

            try {
                PagedModel<EntityModel<DependencyResponse>> response = dependencyService.getDependenciesByDependencyName(name, pageable);
                log.info("Transaction ID: {}, Dependencies retrieved by name", transactionId);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } finally {
                MDC.clear();
            }
        }
    }