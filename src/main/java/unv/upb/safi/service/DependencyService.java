package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.DependencyRequest;
import unv.upb.safi.domain.dto.response.DependencyResponse;
import unv.upb.safi.domain.entity.Component;
import unv.upb.safi.domain.entity.Dependency;
import unv.upb.safi.exception.entityNotFoundException.ComponentNotFoundException;
import unv.upb.safi.exception.entityNotFoundException.DependencyNotFoundException;
import unv.upb.safi.repository.ComponentRepository;
import unv.upb.safi.repository.DependencyRepository;


@Service
public class DependencyService {

    private final DependencyRepository dependencyRepository;
    private final ComponentRepository componentRepository;
    private final Logger logger = LoggerFactory.getLogger(DependencyService.class);

    @Autowired
    public DependencyService(DependencyRepository dependencyRepository, ComponentRepository componentRepository) {
        this.dependencyRepository = dependencyRepository;
        this.componentRepository = componentRepository;
    }

    @Transactional
    public DependencyResponse createDependency(DependencyRequest dependencyRequest) {
        logger.info("Transaction ID: {}, Adding dependency {}",
                MDC.get("transactionId"), dependencyRequest.getDependencyName());

        Component component = getComponentByIdOrThrow(dependencyRequest.getComponentId());
        Dependency dependency = new Dependency();
        dependency.setDependencyName(dependencyRequest.getDependencyName());
        dependency.setDependencyDescription(dependencyRequest.getDependencyDescription());
        dependency.setComponent(component);

        dependency = dependencyRepository.save(dependency);

        logger.info("Transaction ID: {}, Dependency added successfully", MDC.get("transactionId"));

        return mapToResponse(dependency);
    }

    @Transactional
    public void deleteDependency(Long id) {
        logger.info("Transaction ID: {}, Deleting dependency with id {}", MDC.get("transactionId"), id);

        Dependency dependency = getDependencyByIdOrThrow(id);

        dependencyRepository.delete(dependency);

        logger.info("Transaction ID: {}, Deleted dependency {}", MDC.get("transactionId"), id);
    }

    public DependencyResponse updateDependency(Long dependencyId, DependencyRequest dependencyRequest) {
        logger.info("Transaction ID: {}, Updating dependency with id {}", MDC.get("transactionId"), dependencyId);

        Dependency dependency = getDependencyByIdOrThrow(dependencyId);

        Component component = getComponentByIdOrThrow(dependencyRequest.getComponentId());

        dependency.setDependencyName(dependencyRequest.getDependencyName());
        dependency.setDependencyDescription(dependencyRequest.getDependencyDescription());
        dependency.setComponent(component);

        dependency = dependencyRepository.save(dependency);

        return mapToResponse(dependency);
    }

    public Page<DependencyResponse> getDependenciesByComponentId(Long componentId, int page, int size, String sortBy, String direction) {
        logger.info("Transaction ID: {}, Getting dependencies for component with id {}",
                MDC.get("transactionId"), componentId);

        if (!componentRepository.existsById(componentId)) {
            throw new ComponentNotFoundException(componentId.toString());
        }

        return dependencyRepository.findByComponent_ComponentId(
                componentId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy))
        ).map(this::mapToResponse);
    }

    private DependencyResponse mapToResponse(Dependency dependency) {
        return new DependencyResponse(
                dependency.getDependencyId(),
                dependency.getDependencyName(),
                dependency.getComponent().getComponentName(),
                dependency.getDependencyDescription()
        );
    }

    private Dependency getDependencyByIdOrThrow(Long dependencyId) {
        return dependencyRepository.findById(dependencyId)
                .orElseThrow(() -> new DependencyNotFoundException(dependencyId.toString()));
    }

    private Component getComponentByIdOrThrow(Long componentId) {
        return componentRepository.findById(componentId)
                .orElseThrow(() -> new ComponentNotFoundException(componentId.toString()));
    }
}

