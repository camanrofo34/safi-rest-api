package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.Executive;

public interface ExecutiveRepository extends JpaRepository<Executive, Long> {
}
