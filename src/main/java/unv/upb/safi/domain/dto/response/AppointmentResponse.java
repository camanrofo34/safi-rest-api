package unv.upb.safi.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import unv.upb.safi.domain.entity.Appointment;

import java.time.LocalDateTime;

@AllArgsConstructor
public class AppointmentResponse {

    private Long AppointmentId;
    private LocalDateTime appointmentTime;
    private String appointmentType;
    private String appointmentStatus;
    private Long executiveId;
    private Long studentId;

}
