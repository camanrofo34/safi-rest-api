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
    public ResponseEntity<DepartmentResponse> create(@Validated @RequestBody DepartmentRequest departmentRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Adding department {}", transactionId, departmentRequest.getDepartmentName());
        MDC.put("transactionId", transactionId.toString());

        try {
            DepartmentResponse response = departmentService.createDepartment(departmentRequest);
            log.info("Transaction ID: {}, Department added", transactionId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            MDC.remove("transactionId");
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
            MDC.remove("transactionId");
        }
    }

    @PutMapping("/{departmentId:\\d+}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable Long departmentId, @Valid @RequestBody DepartmentRequest departmentRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating department with id {}", transactionId, departmentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            DepartmentResponse response = departmentService.updateDepartment(departmentId, departmentRequest);
            log.info("Transaction ID: {}, Department updated", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.remove("transactionId");
        }
    }

    @GetMapping("/{departmentId:\\d+}")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable Long departmentId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Getting department with id {}", transactionId, departmentId);
        MDC.put("transactionId", transactionId.toString());

        try {
            DepartmentResponse response = departmentService.getDepartmentById(departmentId);
            log.info("Transaction ID: {}, Department retrieved", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.remove("transactionId");
        }
    }

    @GetMapping
    public ResponseEntity<Page<DepartmentResponse>> getDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "departmentId") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction){
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching all departments", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            Page<DepartmentResponse> response = departmentService.getDepartments(page, size, sortBy, direction);
            log.info("Transaction ID: {}, Departments retrieved", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.remove("transactionId");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DepartmentResponse>> getDepartmentsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "departmentId") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Searching departments by name '{}'", transactionId, name);
        MDC.put("transactionId", transactionId.toString());

        try {
            Page<DepartmentResponse> response = departmentService.getDepartmentsByName(name, page, size, sortBy, direction);
            log.info("Transaction ID: {}, Departments retrieved by name", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.remove("transactionId");
        }
    }

}
