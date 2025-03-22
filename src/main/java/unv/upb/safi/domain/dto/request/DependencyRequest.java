package unv.upb.safi.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DependencyRequest {

    @NotBlank
    private String dependencyName;

    @NotNull
    private Long componentId;

    @NotBlank
    private String dependencyDescription;

}
