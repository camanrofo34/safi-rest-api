package unv.upb.safi.domain.dto.response;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class StudentResponse {

    private Long studentId;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private List<String> roles;

    private List<String> facultyName;
}
