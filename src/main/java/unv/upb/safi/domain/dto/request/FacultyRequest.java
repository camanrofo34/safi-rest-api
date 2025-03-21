package unv.upb.safi.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FacultyRequest {

    @NotBlank
    private String facultyName;

    @NotNull
    private Long collegeId;
}
