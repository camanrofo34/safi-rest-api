package unv.upb.safi.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DependencyResponse {

    private Long dependencyId;

    private String dependencyName;

    private String componentName;

    private String dependencyDescription;
}
