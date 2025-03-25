package unv.upb.safi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @NonNull
    Page<Teacher> findAll(@NonNull Pageable pageable);

    @NonNull
    @Query("SELECT t FROM Teacher t " +
            "WHERE UPPER(t.user.firstName) LIKE UPPER(CONCAT('%', :userSearch, '%'))" +
            "OR UPPER(t.user.lastName) LIKE UPPER(CONCAT('%', :userSearch, '%')) ")
    Page<Teacher> findByTeacherNameContainingIgnoreCase(@NonNull String name, @NonNull Pageable pageable);
}
