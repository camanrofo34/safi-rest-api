package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Dependency;

import java.util.List;

@Repository
public interface DependencyRepository extends JpaRepository<Dependency, Long> {

    List<Dependency> findByComponent_ComponentId(Long componentComponentId);

}
