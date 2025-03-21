package unv.upb.safi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Executive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long executiveId;

    @OneToOne
    @JoinColumn(nullable = false, unique = true)
    @Setter
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    @Setter
    private Department department;
}
