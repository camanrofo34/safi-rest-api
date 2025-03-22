package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import unv.upb.safi.domain.dto.request.WeeklyMenuRequest;
import unv.upb.safi.domain.dto.response.WeeklyMenuResponse;

import java.util.List;
import java.util.Set;

public interface WeeklyMenuService {
    @Transactional
    WeeklyMenuResponse createWeeklyMenu (WeeklyMenuRequest weeklyMenuRequest);

    WeeklyMenuResponse updateWeeklyMenu(Long weeklyMenuId, WeeklyMenuRequest weeklyMenuRequest);

    @Transactional
    void deleteWeeklyMenu(Long weeklyMenuId);

    WeeklyMenuResponse getWeeklyMenu(Long weeklyMenuId);

    Set<WeeklyMenuResponse> getAllWeeklyMenu();
}
