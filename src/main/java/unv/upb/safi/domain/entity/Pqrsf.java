package unv.upb.safi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Student student;

    @Column(nullable = false, length = 15)
    private String requestType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date submissionDate;
}
