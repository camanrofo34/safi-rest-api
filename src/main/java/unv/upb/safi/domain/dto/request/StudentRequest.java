package unv.upb.safi.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class StudentRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @Setter
    private String password;

    @NotNull
    private List<Long> rolesId;

    @NotNull
    private List<Long> facultyId;
}
