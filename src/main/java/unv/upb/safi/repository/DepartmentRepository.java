package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
