package unv.upb.safi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Dependency;

import java.util.List;

@Repository
public interface DependencyRepository extends JpaRepository<Dependency, Long> {

    @NonNull
    Page<Dependency> findByComponent_ComponentId(@NonNull Long componentComponentId, @NonNull Pageable pageable);

}
