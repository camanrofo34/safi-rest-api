package unv.upb.safi.domain.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
public class StudentResponse {

    private Long studentId;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private List<String> roles;

    private String facultyName;
}
