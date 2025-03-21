package unv.upb.safi.domain.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WeeklyMenuRequest {

    @NotBlank
    private String restaurantName;

    @NotBlank
    private String menuLink;

    @NotBlank
    private String restaurantLink;
}
