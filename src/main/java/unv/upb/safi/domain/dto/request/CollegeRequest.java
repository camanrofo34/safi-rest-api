package unv.upb.safi.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class CollegeRequest {

    @NotBlank
    private String name;

    @NotNull
    private List<String> facultiesNames;
}
