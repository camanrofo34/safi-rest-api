package unv.upb.safi.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class PqrsfRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private String requestType;

    @NotNull
    private String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date submissionDate;


}
