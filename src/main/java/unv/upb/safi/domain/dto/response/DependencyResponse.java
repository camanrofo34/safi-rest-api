package unv.upb.safi.domain.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DependencyResponse {

    private Long dependencyId;

    private String dependencyName;

    private String componentName;
}
