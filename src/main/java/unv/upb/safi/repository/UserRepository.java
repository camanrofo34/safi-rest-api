package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @NonNull
    Optional<User> findByUsername(@NonNull String username);

    @NonNull
    Optional<User> findByEmail(@NonNull String email);

}
