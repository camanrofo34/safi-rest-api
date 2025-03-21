package unv.upb.safi.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DepartmentRequest {

    @NotBlank
    private String departmentName;
}
