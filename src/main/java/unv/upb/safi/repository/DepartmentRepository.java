package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Department;

import java.util.List;

@Repository

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByDepartmentNameIgnoreCase(String name);
}
