package unv.upb.safi.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
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
    public ResponseEntity<PagedModel<EntityModel<RoleResponse>>> getAllRoles(
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching all roles", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<RoleResponse>> roles = roleService.getAllRoles(pageable);
            log.info("Transaction ID: {}, All roles fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(roles);
        } finally {
            MDC.clear();
        }
    }
}