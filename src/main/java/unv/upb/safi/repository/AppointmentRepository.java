package unv.upb.safi.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Appointment;
import unv.upb.safi.domain.entity.Executive;
import unv.upb.safi.domain.entity.Student;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @NonNull
    List<Appointment> findByStudentAndAppointmentTimeBetween(@NonNull Student student,
                                                             @NonNull LocalDateTime start,
                                                             @NonNull LocalDateTime end,
                                                             Sort sort);

    @NonNull
    List<Appointment> findByExecutiveAndAppointmentTimeBetween(@NonNull Executive executive,
                                                               @NonNull LocalDateTime start,
                                                               @NonNull LocalDateTime end,
                                                               Sort sort);

    boolean existsByStudentAndAppointmentTime(Student student, LocalDateTime appointmentTime);
}
