package unv.upb.safi.domain.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WeeklyMenuResponse {

    private Long weeklyMenuId;

    private String restaurantName;

    private String menuLink;

    private String restaurantLink;
}
