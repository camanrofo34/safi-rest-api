package unv.upb.safi.domain.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DepartmentResponse {

    private Long departmentId;
    private String departmentName;
}
