package unv.upb.safi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Department;

import java.util.List;

@Repository

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @NonNull
    Page<Department> findAll(@NonNull Pageable pageable);

    @NonNull
    Page<Department> findByDepartmentNameContainingIgnoreCase(@NonNull String name, @NonNull Pageable pageable);
}
