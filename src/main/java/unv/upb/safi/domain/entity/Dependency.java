package unv.upb.safi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Dependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dependencyId;

    @Column(nullable = false)
    @Setter
    private String dependencyName;

    @Column(nullable = false)
    @Setter
    private String dependencyDescription;

    @ManyToOne
    @JoinColumn(nullable = false)
    @Setter
    private Component component;
}
