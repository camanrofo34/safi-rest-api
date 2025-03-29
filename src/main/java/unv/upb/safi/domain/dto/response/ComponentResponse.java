package unv.upb.safi.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ComponentResponse {

    private Long componentId;
    private String componentName;
    private String componentDescription;
}
