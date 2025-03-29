package unv.upb.safi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.College;
import unv.upb.safi.domain.entity.Faculty;

import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    @NonNull
    Page<Faculty> findAllByCollege(@NonNull College college, @NonNull Pageable pageable);

    @NonNull
    Page<Faculty> findAllByFacultyNameContainingIgnoreCase(@NonNull String name, @NonNull Pageable pageable);

}
