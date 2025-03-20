package unv.upb.safi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class WeeklyMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weeklyMenuId;

    @Column(nullable = false, unique = true)
    private String restaurantName;

    @Column(nullable = false)
    private String menuLink;

    @Column(nullable = false)
    private String restaurantLink;

}
