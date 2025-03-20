package unv.upb.safi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collegeId;

    @Column(nullable = false)
    @Setter
    private String name;

    @OneToMany(mappedBy = "college", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Setter
    private Map<Long, Faculty> faculties;
}
