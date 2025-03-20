package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
