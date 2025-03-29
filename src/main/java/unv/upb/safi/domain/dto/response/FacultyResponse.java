package unv.upb.safi.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FacultyResponse {

    private Long facultyId;

    private String facultyName;

    private String collegeName;
}
