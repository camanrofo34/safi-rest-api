package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Appointment;
import unv.upb.safi.domain.entity.Executive;
import unv.upb.safi.domain.entity.Student;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByStudent(Student student);

    List<Appointment> findByExecutive(Executive executive);
}
