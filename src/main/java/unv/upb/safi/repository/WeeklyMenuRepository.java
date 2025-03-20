package unv.upb.safi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unv.upb.safi.domain.entity.WeeklyMenu;

public interface WeeklyMenuRepository extends JpaRepository<WeeklyMenu, Long> {
}
