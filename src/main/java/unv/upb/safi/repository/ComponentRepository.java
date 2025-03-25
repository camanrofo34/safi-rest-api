package unv.upb.safi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Component;

import java.util.List;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {

    @NonNull
    Page<Component> findAll(@NonNull Pageable pageable);

    @NonNull
    Page<Component> findByComponentNameContainingIgnoreCase(@NonNull String name, @NonNull Pageable pageable);
}
