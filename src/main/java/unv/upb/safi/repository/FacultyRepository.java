package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}
