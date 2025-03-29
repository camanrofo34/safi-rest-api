package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.hateoas.EntityModel;
import unv.upb.safi.domain.dto.request.WeeklyMenuRequest;
import unv.upb.safi.domain.dto.response.WeeklyMenuResponse;

import java.util.List;
import java.util.Set;

public interface WeeklyMenuService {
    @Transactional
    EntityModel<WeeklyMenuResponse> createWeeklyMenu (WeeklyMenuRequest weeklyMenuRequest);

    EntityModel<WeeklyMenuResponse> updateWeeklyMenu(Long weeklyMenuId, WeeklyMenuRequest weeklyMenuRequest);

    @Transactional
    void deleteWeeklyMenu(Long weeklyMenuId);

    EntityModel<WeeklyMenuResponse> getWeeklyMenu(Long weeklyMenuId);

    Set<EntityModel<WeeklyMenuResponse>> getAllWeeklyMenu();
}
