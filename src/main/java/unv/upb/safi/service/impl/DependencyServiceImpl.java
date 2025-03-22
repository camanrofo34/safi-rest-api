package unv.upb.safi.service.impl;

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
import unv.upb.safi.service.DependencyService;


@Service
public class DependencyServiceImpl implements DependencyService {

    private final DependencyRepository dependencyRepository;
    private final ComponentRepository componentRepository;
    private final Logger logger = LoggerFactory.getLogger(DependencyServiceImpl.class);

    @Autowired
    public DependencyServiceImpl(DependencyRepository dependencyRepository, ComponentRepository componentRepository) {
        this.dependencyRepository = dependencyRepository;
        this.componentRepository = componentRepository;
    }

    @Transactional
    @Override
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
    @Override
    public void deleteDependency(Long id) {
        logger.info("Transaction ID: {}, Deleting dependency with id {}", MDC.get("transactionId"), id);

        Dependency dependency = getDependencyByIdOrThrow(id);

        dependencyRepository.delete(dependency);

        logger.info("Transaction ID: {}, Deleted dependency {}", MDC.get("transactionId"), id);
    }

    @Override
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

    @Override
    public Page<DependencyResponse> getDependenciesByComponentId(Long componentId, int page, int size, String sortBy, Sort.Direction direction) {
        logger.info("Transaction ID: {}, Getting dependencies for component with id {}",
                MDC.get("transactionId"), componentId);

        if (!componentRepository.existsById(componentId)) {
            throw new ComponentNotFoundException(componentId.toString());
        }

        return dependencyRepository.findByComponent_ComponentId(
                componentId,
                PageRequest.of(page, size, Sort.by(direction, sortBy))
        ).map(this::mapToResponse);
    }

    @Override
    public DependencyResponse getDependencyById(Long dependencyId) {
        logger.info("Transaction ID: {}, Getting dependency with id {}", MDC.get("transactionId"), dependencyId);

        Dependency dependency = getDependencyByIdOrThrow(dependencyId);

        return mapToResponse(dependency);
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

