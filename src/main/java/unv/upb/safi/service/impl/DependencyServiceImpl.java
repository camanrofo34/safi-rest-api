package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import unv.upb.safi.util.SearchNormalizerUtil;


@Service
public class DependencyServiceImpl implements DependencyService {

    private final DependencyRepository dependencyRepository;
    private final ComponentRepository componentRepository;
    private final SearchNormalizerUtil searchNormalizerUtil;

    @Autowired
    public DependencyServiceImpl(DependencyRepository dependencyRepository,
                                 ComponentRepository componentRepository,
                                 SearchNormalizerUtil searchNormalizerUtil) {
        this.dependencyRepository = dependencyRepository;
        this.componentRepository = componentRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Transactional
    @Override
    public DependencyResponse createDependency(DependencyRequest dependencyRequest) {

        Component component = getComponentByIdOrThrow(dependencyRequest.getComponentId());
        Dependency dependency = new Dependency();
        dependency.setDependencyName(dependencyRequest.getDependencyName());
        dependency.setDependencyDescription(dependencyRequest.getDependencyDescription());
        dependency.setComponent(component);

        dependency = dependencyRepository.save(dependency);

        return mapToResponse(dependency);
    }

    @Transactional
    @Override
    public void deleteDependency(Long id) {
        Dependency dependency = getDependencyByIdOrThrow(id);

        dependencyRepository.delete(dependency);
    }

    @Override
    public DependencyResponse updateDependency(Long dependencyId, DependencyRequest dependencyRequest) {

        Dependency dependency = getDependencyByIdOrThrow(dependencyId);

        Component component = getComponentByIdOrThrow(dependencyRequest.getComponentId());

        dependency.setDependencyName(dependencyRequest.getDependencyName());
        dependency.setDependencyDescription(dependencyRequest.getDependencyDescription());
        dependency.setComponent(component);

        dependency = dependencyRepository.save(dependency);

        return mapToResponse(dependency);
    }

    @Override
    public Page<DependencyResponse> getDependenciesByComponentId(Long componentId, Pageable pageable) {

        if (!componentRepository.existsById(componentId)) {
            throw new ComponentNotFoundException(componentId.toString());
        }

        return dependencyRepository.findByComponent_ComponentId(componentId, pageable)
        .map(this::mapToResponse);
    }

    @Override
    public Page<DependencyResponse> getDependenciesByDependencyName(String name, Pageable pageable) {
        return dependencyRepository.findByDependencyNameContainingIgnoreCase(
                searchNormalizerUtil.normalize(name), pageable)
                .map(this::mapToResponse);
    }

    @Override
    public DependencyResponse getDependencyById(Long dependencyId) {

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

