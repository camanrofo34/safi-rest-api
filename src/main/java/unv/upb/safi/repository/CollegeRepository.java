package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.College;

public interface CollegeRepository extends JpaRepository<College, Long> {
}
