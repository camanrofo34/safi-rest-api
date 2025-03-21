package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
