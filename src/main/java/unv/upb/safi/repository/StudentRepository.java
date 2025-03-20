package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
