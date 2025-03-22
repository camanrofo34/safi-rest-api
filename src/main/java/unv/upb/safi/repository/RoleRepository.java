package unv.upb.safi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @NonNull
    Page<Role> findAll(@NonNull Pageable pageable);
}
