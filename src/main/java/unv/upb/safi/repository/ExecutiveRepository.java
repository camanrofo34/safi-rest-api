package unv.upb.safi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Executive;

import java.util.Optional;

@Repository
public interface ExecutiveRepository extends JpaRepository<Executive, Long> {

    @NonNull
    Page<Executive> findAll(@NonNull Pageable pageable);

    @NonNull
    Page<Executive> findExecutiveByUser_FirstName(@NonNull String userFirstName, @NonNull Pageable pageable);
}
