package unv.upb.safi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Executive;

import java.util.Optional;

@Repository
public interface ExecutiveRepository extends JpaRepository<Executive, Long> {

    @NonNull
    Page<Executive> findAll(@NonNull Pageable pageable);

    @NonNull
    @Query("SELECT e FROM Executive e " +
            "WHERE UPPER(e.user.firstName) LIKE UPPER(CONCAT('%', :userSearch, '%'))" +
            "OR UPPER(e.user.lastName) LIKE UPPER(CONCAT('%', :userSearch, '%')) ")
    Page<Executive> findAllByExecutiveNameContainingIgnoreCase(@NonNull String userSearch, @NonNull Pageable pageable);
}
