package unv.upb.safi.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
public class AppointmentResponse {

    private Long AppointmentId;
    private LocalDateTime appointmentTime;
    private String appointmentType;
    private String appointmentStatus;
    private Long executiveId;
    private Long studentId;
}
