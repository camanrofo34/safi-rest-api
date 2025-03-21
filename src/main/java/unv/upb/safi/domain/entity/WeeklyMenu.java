package unv.upb.safi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class WeeklyMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weeklyMenuId;

    @Column(nullable = false, unique = true)
    @Setter
    private String restaurantName;

    @Column(nullable = false)
    @Setter
    private String menuLink;

    @Column(nullable = false)
    @Setter
    private String restaurantLink;

}
