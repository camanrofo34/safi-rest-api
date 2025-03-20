package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
