package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import unv.upb.safi.domain.dto.request.AppointmentRequest;
import unv.upb.safi.domain.dto.response.AppointmentResponse;
import unv.upb.safi.exception.AppointmentConflictException;

import java.util.Set;

public interface AppointmentService {

    @Transactional
    AppointmentResponse scheduleAppointment(Long studentId, AppointmentRequest appointmentRequest) throws AppointmentConflictException;

    @Transactional
    void cancelAppointment(Long appointmentId);

    Set<AppointmentResponse> getAppointmentsByStudent(Long studentId,
                                                      int year,
                                                      int month);

    Set<AppointmentResponse> getAppointmentsByExecutive(Long executiveId,
                                                        int year,
                                                        int month);

    AppointmentResponse getAppointmentById(Long appointmentId);
}
