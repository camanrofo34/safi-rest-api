package unv.upb.safi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long componentId;

    @Column(nullable = false, unique = true)
    @Setter
    private String componentName;

    @Column(nullable = false)
    @Setter
    private String componentDescription;

    @OneToMany
    @JoinColumn(name = "component_id")
    @Setter
    private Set<Dependency> dependencies;
}
