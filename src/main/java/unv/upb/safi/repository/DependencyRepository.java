package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Dependency;

@Repository
public interface DependencyRepository extends JpaRepository<Dependency, Long> {
}
