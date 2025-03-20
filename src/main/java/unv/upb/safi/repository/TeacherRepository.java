package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
