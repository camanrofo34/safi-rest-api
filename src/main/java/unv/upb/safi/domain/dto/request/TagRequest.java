package unv.upb.safi.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TagRequest {

    @NotBlank
    private String tagName;
}
