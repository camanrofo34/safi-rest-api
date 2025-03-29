package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.AppointmentController;
import unv.upb.safi.domain.dto.request.AppointmentRequest;
import unv.upb.safi.domain.dto.response.AppointmentResponse;
import unv.upb.safi.domain.entity.Appointment;
import unv.upb.safi.domain.entity.Executive;
import unv.upb.safi.domain.entity.Student;
import unv.upb.safi.exception.AppointmentConflictException;
import unv.upb.safi.exception.entityNotFoundException.AppointmentNotFoundException;
import unv.upb.safi.exception.entityNotFoundException.ExecutiveNotFoundException;
import unv.upb.safi.exception.entityNotFoundException.StudentNotFoundException;
import unv.upb.safi.repository.AppointmentRepository;
import unv.upb.safi.repository.ExecutiveRepository;
import unv.upb.safi.repository.StudentRepository;
import unv.upb.safi.service.AppointmentService;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final StudentRepository studentRepository;
    private final ExecutiveRepository executiveRepository;

    @Autowired
    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            StudentRepository studentRepository,
            ExecutiveRepository executiveRepository) {
        this.appointmentRepository = appointmentRepository;
        this.studentRepository = studentRepository;
        this.executiveRepository = executiveRepository;
    }

    @Transactional
    @Override
    public EntityModel<AppointmentResponse> scheduleAppointment(Long studentId, AppointmentRequest appointmentRequest) throws AppointmentConflictException {

        Student student = getStudentById(studentId);

        Executive executive = getExecutiveById(appointmentRequest.getExecutiveId());

        boolean existsConflict = appointmentRepository.existsByStudentAndAppointmentTime(student, appointmentRequest.getAppointmentTime());

        if (existsConflict) {
            throw new AppointmentConflictException("Student already has an appointment at this time.");
        }

        Appointment appointment = new Appointment();
        appointment.setStudent(student);
        appointment.setExecutive(executive);
        appointment.setAppointmentTime(appointmentRequest.getAppointmentTime());
        appointment.setAppointmentType(appointmentRequest.getAppointmentType());
        appointment.setAppointmentStatus(appointmentRequest.getAppointmentStatus());

        appointment = appointmentRepository.save(appointment);

        return mapToResponse(appointment);
    }

    @Transactional
    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentByIdOrThrow(appointmentId);

        appointmentRepository.delete(appointment);
    }

    @Override
    public Set<EntityModel<AppointmentResponse>> getAppointmentsByStudent(Long studentId,
                                                             int year,
                                                             int month) {
        Student student = getStudentById(studentId);

        Sort sort = Sort.by(Sort.Order.asc("appointmentTime"));
        LocalDateTime start = YearMonth.of(year, month).atDay(1).atStartOfDay();
        LocalDateTime end = YearMonth.of(year, month).atEndOfMonth().atTime(23, 59, 59);

        return appointmentRepository.findByStudentAndAppointmentTimeBetween(
                student,
                start,
                end,
                sort
                        ).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<EntityModel<AppointmentResponse>> getAppointmentsByExecutive(Long executiveId,
                                                               int year,
                                                               int month) {
        Executive executive = getExecutiveById(executiveId);

        Sort sort = Sort.by(Sort.Order.asc("appointmentTime"));
        LocalDateTime start = YearMonth.of(year, month).atDay(1).atStartOfDay();
        LocalDateTime end = YearMonth.of(year, month).atEndOfMonth().atTime(23, 59, 59);

        return appointmentRepository.findByExecutiveAndAppointmentTimeBetween(
                executive,
                start,
                end,
                sort
                ).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    public EntityModel<AppointmentResponse> getAppointmentById(Long appointmentId) {
        Appointment appointment = getAppointmentByIdOrThrow(appointmentId);

        return mapToResponse(appointment);
    }

    private EntityModel<AppointmentResponse> mapToResponse(Appointment appointment) {
        AppointmentResponse appointmentResponse = new AppointmentResponse(
                appointment.getAppointmentId(),
                appointment.getAppointmentTime(),
                appointment.getAppointmentType(),
                appointment.getAppointmentStatus(),
                appointment.getExecutive().getUser().getUserId(),
                appointment.getStudent().getUser().getUserId()
        );

        return mapToEntityModel(appointmentResponse);
    }

    private EntityModel<AppointmentResponse> mapToEntityModel(AppointmentResponse appointmentResponse) {
        return EntityModel.of(appointmentResponse,
                linkTo(methodOn(AppointmentController.class).getAppointmentById(appointmentResponse.getAppointmentId())).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).cancelAppointment(appointmentResponse.getStudentId(), appointmentResponse.getAppointmentId() )).withRel("cancel-Appointment")
        );
    }

    private Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId.toString()));
    }

    private Executive getExecutiveById(Long executiveId) {
        return executiveRepository.findById(executiveId)
                .orElseThrow(() -> new ExecutiveNotFoundException(executiveId.toString()));
    }

    private Appointment getAppointmentByIdOrThrow(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId.toString()));
    }

}
