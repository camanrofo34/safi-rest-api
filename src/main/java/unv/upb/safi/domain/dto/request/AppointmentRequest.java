package unv.upb.safi.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AppointmentRequest {

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentTime;

    @NotBlank
    private String appointmentType;

    @NotBlank
    private String appointmentStatus;

    @NotNull
    private Long executiveId;
}
