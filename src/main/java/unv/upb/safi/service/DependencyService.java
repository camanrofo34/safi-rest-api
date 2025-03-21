package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.DependencyRequest;
import unv.upb.safi.domain.dto.response.DependencyResponse;
import unv.upb.safi.domain.entity.Component;
import unv.upb.safi.domain.entity.Dependency;
import unv.upb.safi.exception.entityNotFoundException.ComponentNotFoundException;
import unv.upb.safi.exception.entityNotFoundException.DependencyNotFoundException;
import unv.upb.safi.repository.ComponentRepository;
import unv.upb.safi.repository.DependencyRepository;

import java.util.List;

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

    public DependencyResponse createDependency(DependencyRequest dependencyRequest) {
        logger.info("Transaction ID: {}, Adding dependency {}",
                MDC.get("transactionId"), dependencyRequest.getDependencyName());

        Component component = componentRepository.findById(dependencyRequest.getComponentId())
                .orElseThrow(() -> new ComponentNotFoundException(dependencyRequest.getComponentId().toString()));

        Dependency dependency = new Dependency();
        dependency.setDependencyName(dependencyRequest.getDependencyName());
        dependency.setComponent(component);
        dependency = dependencyRepository.save(dependency);

        logger.info("Transaction ID: {}, Dependency added successfully", MDC.get("transactionId"));

        return mapToResponse(dependency);
    }

    @Transactional
    public void deleteDependency(Long id) {
        logger.info("Transaction ID: {}, Deleting dependency with id {}", MDC.get("transactionId"), id);

        if (!dependencyRepository.existsById(id)) {
            throw new DependencyNotFoundException(id.toString());
        }

        dependencyRepository.deleteById(id);
        logger.info("Transaction ID: {}, Deleted dependency {}", MDC.get("transactionId"), id);
    }

    public DependencyResponse updateDependency(Long dependencyId, DependencyRequest dependencyRequest) {
        logger.info("Transaction ID: {}, Updating dependency with id {}", MDC.get("transactionId"), dependencyId);

        Dependency dependency = dependencyRepository.findById(dependencyId)
                .orElseThrow(() -> new DependencyNotFoundException(dependencyId.toString()));

        Component component = componentRepository.findById(dependencyRequest.getComponentId())
                .orElseThrow(() -> new ComponentNotFoundException(dependencyRequest.getComponentId().toString()));

        dependency.setDependencyName(dependencyRequest.getDependencyName());
        dependency.setComponent(component);
        dependency = dependencyRepository.save(dependency);

        logger.info("Transaction ID: {}, Dependency updated successfully", MDC.get("transactionId"));
        return mapToResponse(dependency);
    }

    public List<DependencyResponse> getDependenciesByComponentId(Long componentId) {
        logger.info("Transaction ID: {}, Getting dependencies for component with id {}",
                MDC.get("transactionId"), componentId);

        if (!componentRepository.existsById(componentId)) {
            throw new ComponentNotFoundException(componentId.toString());
        }

        return dependencyRepository.findByComponent_ComponentId(componentId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private DependencyResponse mapToResponse(Dependency dependency) {
        return new DependencyResponse(
                dependency.getDependencyId(),
                dependency.getDependencyName(),
                dependency.getComponent().getComponentName()
        );
    }
}

