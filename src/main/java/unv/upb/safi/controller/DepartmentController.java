package unv.upb.safi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unv.upb.safi.domain.dto.request.DepartmentRequest;
import unv.upb.safi.domain.dto.response.DepartmentResponse;
import unv.upb.safi.service.DepartmentService;
import unv.upb.safi.service.impl.DepartmentServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("/departments")
@Validated
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentServiceImpl departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<DepartmentResponse>> create(@Valid @RequestBody DepartmentRequest departmentRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Adding department {}", transactionId, departmentRequest.getDepartmentName());
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<DepartmentResponse> response = departmentService.createDepartment(departmentRequest);
            log.info("Transaction ID: {}, Department added", transactionId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            MDC.clear();
        }
    }

    @DeleteMapping("/{departmentId:\\d+}")
    public ResponseEntity<Void> delete(@PathVariable Long departmentId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Deleting department with id {}", transactionId, departmentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            departmentService.deleteDepartment(departmentId);
            log.info("Transaction ID: {}, Department deleted", transactionId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{departmentId:\\d+}")
    public ResponseEntity<EntityModel<DepartmentResponse>> update(@PathVariable Long departmentId, @Valid @RequestBody DepartmentRequest departmentRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating department with id {}", transactionId, departmentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<DepartmentResponse> response = departmentService.updateDepartment(departmentId, departmentRequest);
            log.info("Transaction ID: {}, Department updated", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/{departmentId:\\d+}")
    public ResponseEntity<EntityModel<DepartmentResponse>> getById(@PathVariable Long departmentId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Getting department with id {}", transactionId, departmentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<DepartmentResponse> response = departmentService.getDepartmentById(departmentId);
            log.info("Transaction ID: {}, Department retrieved", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<DepartmentResponse>>> getDepartments(
            @PageableDefault(size = 10, sort = "departmentId", direction = Sort.Direction.ASC) Pageable pageable) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching all departments", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<DepartmentResponse>> response = departmentService.getDepartments(pageable);
            log.info("Transaction ID: {}, Departments retrieved", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<DepartmentResponse>>> getDepartmentsByName(
            @RequestParam String name,
            @PageableDefault(size = 10, sort = "departmentId", direction = Sort.Direction.ASC) Pageable pageable) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Searching departments by name '{}'", transactionId, name);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<DepartmentResponse>> response = departmentService.getDepartmentsByName(name, pageable);
            log.info("Transaction ID: {}, Departments retrieved by name", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }
}
