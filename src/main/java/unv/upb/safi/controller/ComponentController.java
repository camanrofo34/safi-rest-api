package unv.upb.safi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unv.upb.safi.domain.dto.request.ComponentRequest;
import unv.upb.safi.domain.dto.response.ComponentResponse;
import unv.upb.safi.service.ComponentService;
import unv.upb.safi.service.impl.ComponentServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("/component")
@Validated
@Slf4j
public class ComponentController {

    private final ComponentService componentService;

    @Autowired
    public ComponentController(ComponentServiceImpl componentService) {
        this.componentService = componentService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<ComponentResponse>> createComponent(@Valid @RequestBody ComponentRequest componentRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Adding component {}", transactionId, componentRequest.getComponentName());
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<ComponentResponse> response = componentService.createComponent(componentRequest);
            log.info("Transaction ID: {}, Component added", transactionId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            MDC.clear();
        }
    }

    @DeleteMapping("/{componentId:\\d+}")
    public ResponseEntity<Void> deleteComponent(@PathVariable Long componentId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Deleting component with id {}", transactionId, componentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            componentService.deleteComponent(componentId);
            log.info("Transaction ID: {}, Component deleted", transactionId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{componentId:\\d+}")
    public ResponseEntity<EntityModel<ComponentResponse>> updateComponent(@PathVariable Long componentId, @Valid @RequestBody ComponentRequest componentRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating component with id {}", transactionId, componentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<ComponentResponse> response = componentService.updateComponent(componentId, componentRequest);
            log.info("Transaction ID: {}, Component updated", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/{componentId:\\d+}")
    public ResponseEntity<EntityModel<ComponentResponse>> getComponent(@PathVariable Long componentId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Getting component with id {}", transactionId, componentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<ComponentResponse> response = componentService.getComponent(componentId);
            log.info("Transaction ID: {}, Component retrieved", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ComponentResponse>>> getAllComponents(
            @PageableDefault(size = 10, sort = "componentId", direction = Sort.Direction.DESC) Pageable pageable) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Getting all components", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<ComponentResponse>> response = componentService.getComponents(pageable);
            log.info("Transaction ID: {}, Components retrieved", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<ComponentResponse>>> searchByComponentName(@RequestParam String name,
                                                                         @PageableDefault(size = 10, sort = "componentId", direction = Sort.Direction.DESC) Pageable pageable) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Searching components by name '{}'", transactionId, name);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<ComponentResponse>> response = componentService.getComponentsByName(name, pageable);
            log.info("Transaction ID: {}, Components retrieved by name", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }
}
