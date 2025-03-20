package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.Dependency;

public interface DependencyRepository extends JpaRepository<Dependency, Long> {
}
