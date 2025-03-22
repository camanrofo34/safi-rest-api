package unv.upb.safi.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unv.upb.safi.domain.dto.response.RoleResponse;
import unv.upb.safi.service.RoleService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/roles")
@Validated
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<Page<RoleResponse>> getAllRoles(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching all roles", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            Page<RoleResponse> roles = roleService.getAllRoles(page, size, sortBy, direction);
            log.info("Transaction ID: {}, All roles fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(roles);
        } finally {
            MDC.clear();
        }
    }
}