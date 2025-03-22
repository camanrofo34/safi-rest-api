package unv.upb.safi.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ComponentRequest {

    @NotBlank
    private String componentName;

    @NotBlank
    private String componentDescription;
}
