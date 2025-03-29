package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.WeeklyMenuController;
import unv.upb.safi.domain.dto.request.WeeklyMenuRequest;
import unv.upb.safi.domain.dto.response.WeeklyMenuResponse;
import unv.upb.safi.domain.entity.WeeklyMenu;
import unv.upb.safi.exception.entityNotFoundException.WeeklyMenuNotFoundException;
import unv.upb.safi.repository.WeeklyMenuRepository;
import unv.upb.safi.service.WeeklyMenuService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class WeeklyMenuServiceImpl implements WeeklyMenuService {

    private final WeeklyMenuRepository weeklyMenuRepository;

    @Autowired
    public WeeklyMenuServiceImpl(WeeklyMenuRepository weeklyMenuRepository) {
        this.weeklyMenuRepository = weeklyMenuRepository;
    }

    @Override
    public EntityModel<WeeklyMenuResponse> createWeeklyMenu(WeeklyMenuRequest weeklyMenuRequest) {
            WeeklyMenu weeklyMenu = new WeeklyMenu();
            weeklyMenu.setRestaurantName(weeklyMenuRequest.getRestaurantName());
            weeklyMenu.setRestaurantLink(weeklyMenuRequest.getRestaurantLink());
            weeklyMenu.setMenuLink(weeklyMenuRequest.getMenuLink());
            weeklyMenu = weeklyMenuRepository.save(weeklyMenu);

            System.out.println(weeklyMenu.getRestaurantName());
            System.out.println(weeklyMenu.getRestaurantLink());
            System.out.println(weeklyMenu.getMenuLink());

            return mapToResponse(weeklyMenu);
    }

    @Override
    public EntityModel<WeeklyMenuResponse> updateWeeklyMenu(Long weeklyMenuId, WeeklyMenuRequest weeklyMenuRequest) {
        WeeklyMenu weeklyMenu = getWeeklyMenuByIdOrThrow(weeklyMenuId);

        weeklyMenu.setRestaurantName(weeklyMenuRequest.getRestaurantName());
        weeklyMenu.setRestaurantLink(weeklyMenuRequest.getRestaurantLink());
        weeklyMenu.setMenuLink(weeklyMenuRequest.getMenuLink());
        weeklyMenu = weeklyMenuRepository.save(weeklyMenu);

        return mapToResponse(weeklyMenu);
    }

    @Transactional
    @Override
    public void deleteWeeklyMenu(Long weeklyMenuId) {
        WeeklyMenu weeklyMenu = getWeeklyMenuByIdOrThrow(weeklyMenuId);

        weeklyMenuRepository.delete(weeklyMenu);
    }

    @Override
    public EntityModel<WeeklyMenuResponse> getWeeklyMenu(Long weeklyMenuId) {
        WeeklyMenu weeklyMenu = getWeeklyMenuByIdOrThrow(weeklyMenuId);

        return mapToResponse(weeklyMenu);
    }

    @Override
    public Set<EntityModel<WeeklyMenuResponse>> getAllWeeklyMenu(){
        List<WeeklyMenu> weeklyMenus = weeklyMenuRepository.findAll();

        return weeklyMenus.stream().map(this::mapToResponse)
                .collect(Collectors.toSet());
    }

    private EntityModel<WeeklyMenuResponse> mapToResponse(WeeklyMenu weeklyMenu) {
        WeeklyMenuResponse weeklyMenuResponse = new WeeklyMenuResponse(
                weeklyMenu.getWeeklyMenuId(),
                weeklyMenu.getRestaurantName(),
                weeklyMenu.getMenuLink(),
                weeklyMenu.getRestaurantLink()
        );

        return EntityModel.of(weeklyMenuResponse,
                linkTo(methodOn(WeeklyMenuController.class).getWeeklyMenu(weeklyMenuResponse.getWeeklyMenuId())).withSelfRel()
        );
    }

    private WeeklyMenu getWeeklyMenuByIdOrThrow(Long weeklyMenuId) {
        return weeklyMenuRepository.findById(weeklyMenuId)
                .orElseThrow(() -> new WeeklyMenuNotFoundException(weeklyMenuId.toString()));
    }
}
