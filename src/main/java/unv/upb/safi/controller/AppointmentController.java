package unv.upb.safi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unv.upb.safi.domain.dto.request.AppointmentRequest;
import unv.upb.safi.domain.dto.response.AppointmentResponse;
import unv.upb.safi.service.AppointmentService;
import unv.upb.safi.service.impl.AppointmentServiceImpl;

import java.util.Set;
import java.util.UUID;

@RestController
@Validated
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentServiceImpl appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/student/{studentId:\\d+}/appointment")
    public ResponseEntity<AppointmentResponse> scheduleAppointment(@PathVariable Long studentId,
                                                   @RequestBody @Valid AppointmentRequest appointmentRequest){
        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());

        log.info("Transaction ID: {}, Student {} is scheduling appointment with executive {}",
                transactionId ,studentId, appointmentRequest.getExecutiveId());
        try {
            AppointmentResponse appointmentResponse = appointmentService.scheduleAppointment(studentId, appointmentRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointmentResponse);
        } finally {
            MDC.clear();
        }
    }

    @DeleteMapping("/student/{studentId:\\d+}/appointment/{appointmentId:\\d+}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long studentId, @PathVariable Long appointmentId) {
        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());

        log.info("Transaction ID: {}, Student {} is cancelling appointment with id {}",
                transactionId, studentId, appointmentId);
        try{
            appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/student/{studentId:\\d+}/appointment")
    public ResponseEntity<Set<AppointmentResponse>> getAppointmentsForStudent(@PathVariable Long studentId,
                                                                   @RequestParam Integer year,
                                                                   @RequestParam Integer month) {
        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());

        log.info("Transaction ID: {}, Student {} is fetching appointments",
                transactionId, studentId);
        try{
            Set<AppointmentResponse> appointmentResponse = appointmentService.getAppointmentsByStudent(studentId, year, month);
            return ResponseEntity.status(HttpStatus.OK).body(appointmentResponse);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/executive/{executiveId:\\d+}/appointment")
    public ResponseEntity<Set<AppointmentResponse>> getAppointmentsForExecutive(@PathVariable Long executiveId,
                                                                   @RequestParam Integer year,
                                                                   @RequestParam Integer month) {
        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());

        log.info("Transaction ID: {}, Executive {} is fetching appointments",
                transactionId, executiveId);

        try {
            Set<AppointmentResponse> appointmentResponse = appointmentService.getAppointmentsByExecutive(executiveId, year, month);
            return ResponseEntity.status(HttpStatus.OK).body(appointmentResponse);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/appointment/{appointmentId:\\d+}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long appointmentId) {
        UUID transactionId = UUID.randomUUID();
        MDC.put("transactionId", transactionId.toString());

        log.info("Transaction ID: {}, Fetching appointment with id {}",
                transactionId, appointmentId);

        try {
            AppointmentResponse appointmentResponse = appointmentService.getAppointmentById(appointmentId);
            return ResponseEntity.status(HttpStatus.OK).body(appointmentResponse);
        } finally {
            MDC.clear();
        }
    }
}
