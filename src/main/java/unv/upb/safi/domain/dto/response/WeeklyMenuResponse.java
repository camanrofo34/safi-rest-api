package unv.upb.safi.domain.dto.response;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class WeeklyMenuResponse {

    private Long weeklyMenuId;

    private String restaurantName;

    private String menuLink;

    private String restaurantLink;
}
