package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Executive;

import java.util.Optional;

@Repository
public interface ExecutiveRepository extends JpaRepository<Executive, Long> {

    Optional<Executive> findExecutiveByUser_Username(String userUsername);
}
