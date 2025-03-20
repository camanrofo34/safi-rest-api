package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
