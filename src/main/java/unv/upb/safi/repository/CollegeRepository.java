package unv.upb.safi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.College;




@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {

    @NonNull
    Page<College> findAll(@NonNull Pageable pageable);

    @NonNull
    Page<College> findAllByNameContainingIgnoreCase(@NonNull String name, @NonNull Pageable pageable);
}
