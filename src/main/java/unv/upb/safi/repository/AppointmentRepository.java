package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
