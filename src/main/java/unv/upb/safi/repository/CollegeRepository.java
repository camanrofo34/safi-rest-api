package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.College;

import java.util.List;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {

    List<College> findByNameIgnoreCase(String name);
}
