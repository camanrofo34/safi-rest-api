package unv.upb.safi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private LocalDateTime appointmentTime;

    @Column(nullable = false)
    private String appointmentType;

    @Column(nullable = false)
    private String appointmentStatus;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Executive executive;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

}
