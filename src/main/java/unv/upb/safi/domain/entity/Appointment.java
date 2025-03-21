package unv.upb.safi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Setter
    private LocalDateTime appointmentTime;

    @Column(nullable = false)
    @Setter
    private String appointmentType;

    @Column(nullable = false)
    @Setter
    private String appointmentStatus;

    @ManyToOne
    @JoinColumn(nullable = false)
    @Setter
    private Executive executive;

    @ManyToOne
    @JoinColumn(nullable = false)
    @Setter
    private Student student;

}
