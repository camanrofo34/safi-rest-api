package unv.upb.safi.domain.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TeacherResponse {

    private Long teacherId;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private List<String> roles;

    private String collegeName;
}
