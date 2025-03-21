package unv.upb.safi.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public class FacultyRequest {

    @NotBlank
    private String facultyName;

    @NotBlank
    private String collegeName;
}
