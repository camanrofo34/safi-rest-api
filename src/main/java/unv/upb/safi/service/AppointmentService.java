package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.hateoas.EntityModel;
import unv.upb.safi.domain.dto.request.AppointmentRequest;
import unv.upb.safi.domain.dto.response.AppointmentResponse;
import unv.upb.safi.exception.AppointmentConflictException;

import java.util.Set;

public interface AppointmentService {

    @Transactional
    EntityModel<AppointmentResponse> scheduleAppointment(Long studentId, AppointmentRequest appointmentRequest) throws AppointmentConflictException;

    @Transactional
    void cancelAppointment(Long appointmentId);

    Set<EntityModel<AppointmentResponse>> getAppointmentsByStudent(Long studentId,
                                                      int year,
                                                      int month);

    Set<EntityModel<AppointmentResponse>> getAppointmentsByExecutive(Long executiveId,
                                                        int year,
                                                        int month);

    EntityModel<AppointmentResponse> getAppointmentById(Long appointmentId);
}
