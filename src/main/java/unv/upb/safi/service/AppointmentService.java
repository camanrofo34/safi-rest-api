package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.AppointmentRequest;
import unv.upb.safi.domain.dto.response.AppointmentResponse;
import unv.upb.safi.domain.entity.Appointment;
import unv.upb.safi.domain.entity.Executive;
import unv.upb.safi.domain.entity.Student;
import unv.upb.safi.exception.entityNotFoundException.AppointmentNotFoundException;
import unv.upb.safi.exception.entityNotFoundException.ExecutiveNotFoundException;
import unv.upb.safi.exception.entityNotFoundException.StudentNotFoundException;
import unv.upb.safi.repository.AppointmentRepository;
import unv.upb.safi.repository.ExecutiveRepository;
import unv.upb.safi.repository.StudentRepository;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final StudentRepository studentRepository;
    private final ExecutiveRepository executiveRepository;
    private final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    public AppointmentService(
            AppointmentRepository appointmentRepository,
            StudentRepository studentRepository,
            ExecutiveRepository executiveRepository) {
        this.appointmentRepository = appointmentRepository;
        this.studentRepository = studentRepository;
        this.executiveRepository = executiveRepository;
    }

    public AppointmentResponse scheduleAppointment(Long studentId, AppointmentRequest appointmentRequest) {
        logger.info("Transaction ID: {}, Student {} is scheduling an appointment with executive {}",
                MDC.get("transactionId"), studentId, appointmentRequest.getExecutiveId());

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId.toString()));

        Executive executive = executiveRepository.findById(appointmentRequest.getExecutiveId())
                .orElseThrow(() -> new ExecutiveNotFoundException(appointmentRequest.getExecutiveId().toString()));

        Appointment appointment = new Appointment();
        appointment.setStudent(student);
        appointment.setExecutive(executive);
        appointment.setAppointmentTime(appointmentRequest.getAppointmentTime());
        appointment.setAppointmentType(appointmentRequest.getAppointmentType());
        appointment.setAppointmentStatus(appointmentRequest.getAppointmentStatus());

        appointment = appointmentRepository.save(appointment);
        logger.info("Transaction ID: {}, Appointment scheduled successfully", MDC.get("transactionId"));

        return mapToResponse(appointment);
    }

    @Transactional
    public void cancelAppointment(Long appointmentId) {
        logger.info("Transaction ID: {}, Cancelling appointment {}",
                MDC.get("transactionId"), appointmentId);

        if (!appointmentRepository.existsById(appointmentId)) {
            throw new AppointmentNotFoundException(appointmentId.toString());
        }

        appointmentRepository.deleteById(appointmentId);
        logger.info("Transaction ID: {}, Appointment {} cancelled successfully", MDC.get("transactionId"), appointmentId);
    }

    public List<AppointmentResponse> getAppointmentsByStudent(Long studentId) {
        logger.info("Transaction ID: {}, Fetching appointments for student {}",
                MDC.get("transactionId"), studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId.toString()));

        return appointmentRepository.findByStudent(student).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AppointmentResponse> getAppointmentsByExecutive(Long executiveId) {
        logger.info("Transaction ID: {}, Fetching appointments for executive {}",
                MDC.get("transactionId"), executiveId);

        Executive executive = executiveRepository.findById(executiveId)
                .orElseThrow(() -> new ExecutiveNotFoundException(executiveId.toString()));

        return appointmentRepository.findByExecutive(executive).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AppointmentResponse getAppointmentById(Long appointmentId) {
        logger.info("Transaction ID: {}, Fetching appointment with id {}",
                MDC.get("transactionId"), appointmentId);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId.toString()));

        return mapToResponse(appointment);
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getAppointmentId(),
                appointment.getAppointmentTime(),
                appointment.getAppointmentType(),
                appointment.getAppointmentStatus(),
                appointment.getExecutive().getUser().getUserId(),
                appointment.getStudent().getUser().getUserId()
        );
    }
}
