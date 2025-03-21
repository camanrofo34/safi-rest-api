package unv.upb.safi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
public class Pqrsf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pqrsfId;

    @ManyToOne
    @JoinColumn(name = "student_Id", nullable = false)
    @Setter
    private Student student;

    @Column(nullable = false, length = 15)
    @Setter
    private String requestType;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Setter
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Setter
    private Date submissionDate;
}
