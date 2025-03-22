package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.WeeklyMenuRequest;
import unv.upb.safi.domain.dto.response.WeeklyMenuResponse;
import unv.upb.safi.domain.entity.WeeklyMenu;
import unv.upb.safi.exception.entityNotFoundException.WeeklyMenuNotFoundException;
import unv.upb.safi.repository.WeeklyMenuRepository;
import unv.upb.safi.service.WeeklyMenuService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WeeklyMenuServiceImpl implements WeeklyMenuService {

    private final WeeklyMenuRepository weeklyMenuRepository;

    private final Logger logger = LoggerFactory.getLogger(WeeklyMenuServiceImpl.class);

    @Autowired
    public WeeklyMenuServiceImpl(WeeklyMenuRepository weeklyMenuRepository) {
        this.weeklyMenuRepository = weeklyMenuRepository;
    }

    @Transactional
    @Override
    public WeeklyMenuResponse createWeeklyMenu(WeeklyMenuRequest weeklyMenuRequest) {
        logger.info("Transaction ID: {}, Adding weekly menu {}",
                MDC.get("transactionId") ,weeklyMenuRequest.getRestaurantName());

            WeeklyMenu weeklyMenu = new WeeklyMenu();
            weeklyMenu.setRestaurantName(weeklyMenuRequest.getRestaurantName());
            weeklyMenu.setRestaurantLink(weeklyMenuRequest.getRestaurantLink());
            weeklyMenu.setMenuLink(weeklyMenuRequest.getMenuLink());
            weeklyMenu = weeklyMenuRepository.save(weeklyMenu);

            logger.info("Transaction ID: {}, Weekly menu added", MDC.get("transactionId"));
            return mapToResponse(weeklyMenu);
    }

    @Override
    public WeeklyMenuResponse updateWeeklyMenu(Long weeklyMenuId, WeeklyMenuRequest weeklyMenuRequest) {
        logger.info("Transaction ID: {}, Updating weekly menu {}",
                MDC.get("transactionId"), weeklyMenuRequest.getRestaurantName());

        WeeklyMenu weeklyMenu = getWeeklyMenuByIdOrThrow(weeklyMenuId);

        weeklyMenu.setRestaurantName(weeklyMenuRequest.getRestaurantName());
        weeklyMenu.setRestaurantLink(weeklyMenuRequest.getRestaurantLink());
        weeklyMenu.setMenuLink(weeklyMenuRequest.getMenuLink());
        weeklyMenuRepository.save(weeklyMenu);

        logger.info("Transaction ID: {}, Weekly menu updated", MDC.get("transactionId"));
            return mapToResponse(weeklyMenu);
    }

    @Transactional
    @Override
    public void deleteWeeklyMenu(Long weeklyMenuId) {
        logger.info("Transaction ID: {}, Deleting weekly menu with id: {}", MDC.get("transactionId"), weeklyMenuId);

        WeeklyMenu weeklyMenu = getWeeklyMenuByIdOrThrow(weeklyMenuId);

        weeklyMenuRepository.delete(weeklyMenu);

        logger.info("Transaction ID: {}, Deleted weekly menu with id: {}", MDC.get("transactionId"), weeklyMenuId);
    }

    @Override
    public WeeklyMenuResponse getWeeklyMenu(Long weeklyMenuId) {
        logger.info("Transaction ID: {}, Getting weekly menu with id: {}", MDC.get("transactionId"), weeklyMenuId);

            WeeklyMenu weeklyMenu = getWeeklyMenuByIdOrThrow(weeklyMenuId);
            return mapToResponse(weeklyMenu);
    }

    @Override
    public Set<WeeklyMenuResponse> getAllWeeklyMenu(){
        logger.info("Transaction ID: {}, Getting all weekly menu", MDC.get("transactionId"));
            List<WeeklyMenu> weeklyMenus = weeklyMenuRepository.findAll();

            return weeklyMenus.stream().map(
                    this::mapToResponse
            ).collect(Collectors.toSet());
    }

    private WeeklyMenuResponse mapToResponse(WeeklyMenu weeklyMenu) {
        return new WeeklyMenuResponse(
                weeklyMenu.getWeeklyMenuId(),
                weeklyMenu.getRestaurantName(),
                weeklyMenu.getMenuLink(),
                weeklyMenu.getRestaurantLink()
        );
    }

    private WeeklyMenu getWeeklyMenuByIdOrThrow(Long weeklyMenuId) {
        return weeklyMenuRepository.findById(weeklyMenuId)
                .orElseThrow(() -> new WeeklyMenuNotFoundException(weeklyMenuId.toString()));
    }
}
