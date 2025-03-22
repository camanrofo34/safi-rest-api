package unv.upb.safi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unv.upb.safi.domain.dto.request.WeeklyMenuRequest;
import unv.upb.safi.domain.dto.response.WeeklyMenuResponse;
import unv.upb.safi.service.WeeklyMenuService;

import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/weekly-menus")
@Validated
public class WeeklyMenuController {

    private final WeeklyMenuService weeklyMenuService;

    @Autowired
    public WeeklyMenuController(WeeklyMenuService weeklyMenuService) {
        this.weeklyMenuService = weeklyMenuService;
    }

    @PostMapping
    public ResponseEntity<WeeklyMenuResponse> createWeeklyMenu(@Valid @RequestBody WeeklyMenuRequest weeklyMenuRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Adding weekly menu {}", transactionId, weeklyMenuRequest.getRestaurantName());
        MDC.put("transactionId", transactionId.toString());

        try {
            WeeklyMenuResponse weeklyMenuResponse = weeklyMenuService.createWeeklyMenu(weeklyMenuRequest);
            log.info("Transaction ID: {}, Weekly menu added successfully", transactionId);
            return ResponseEntity.status(HttpStatus.CREATED).body(weeklyMenuResponse);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{weeklyMenuId:\\d+}")
    public ResponseEntity<WeeklyMenuResponse> updateWeeklyMenu(@PathVariable Long weeklyMenuId, @Valid @RequestBody WeeklyMenuRequest weeklyMenuRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating weekly menu with ID {}", transactionId, weeklyMenuId);
        MDC.put("transactionId", transactionId.toString());

        try {
            WeeklyMenuResponse weeklyMenuResponse = weeklyMenuService.updateWeeklyMenu(weeklyMenuId, weeklyMenuRequest);
            log.info("Transaction ID: {}, Weekly menu with ID {} updated successfully", transactionId, weeklyMenuId);
            return ResponseEntity.status(HttpStatus.OK).body(weeklyMenuResponse);
        } finally {
            MDC.clear();
        }
    }

    @DeleteMapping("/{weeklyMenuId:\\d+}")
    public ResponseEntity<Void> deleteWeeklyMenu(@PathVariable Long weeklyMenuId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Deleting weekly menu with ID {}", transactionId, weeklyMenuId);
        MDC.put("transactionId", transactionId.toString());

        try {
            weeklyMenuService.deleteWeeklyMenu(weeklyMenuId);
            log.info("Transaction ID: {}, Weekly menu with ID {} deleted successfully", transactionId, weeklyMenuId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/{weeklyMenuId:\\d+}")
    public ResponseEntity<WeeklyMenuResponse> getWeeklyMenu(@PathVariable Long weeklyMenuId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching weekly menu with ID {}", transactionId, weeklyMenuId);
        MDC.put("transactionId", transactionId.toString());

        try {
            WeeklyMenuResponse weeklyMenuResponse = weeklyMenuService.getWeeklyMenu(weeklyMenuId);
            log.info("Transaction ID: {}, Weekly menu with ID {} fetched successfully", transactionId, weeklyMenuId);
            return ResponseEntity.status(HttpStatus.OK).body(weeklyMenuResponse);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<Set<WeeklyMenuResponse>> getAllWeeklyMenus() {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching all weekly menus", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            Set<WeeklyMenuResponse> weeklyMenus = weeklyMenuService.getAllWeeklyMenu();
            log.info("Transaction ID: {}, All weekly menus fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(weeklyMenus);
        } finally {
            MDC.clear();
        }
    }
}