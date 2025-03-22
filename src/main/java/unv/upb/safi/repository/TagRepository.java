package unv.upb.safi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @NonNull
    Page<Tag> findAll(@NonNull Pageable pageable);
}
