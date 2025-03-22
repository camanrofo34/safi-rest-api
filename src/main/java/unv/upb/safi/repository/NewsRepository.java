package unv.upb.safi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import unv.upb.safi.domain.entity.News;
import unv.upb.safi.domain.entity.Tag;

import java.util.List;
import java.util.Set;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @NonNull
    Page<News> findAll(@NonNull Pageable pageable);

    @NonNull
    Page<News> findAllByTags(@NonNull Set<Tag> tags, @NonNull Pageable pageable);

}
