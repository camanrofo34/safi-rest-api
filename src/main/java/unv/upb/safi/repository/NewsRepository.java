package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.News;

public interface NewsRepository extends JpaRepository<News, Long> {
}
