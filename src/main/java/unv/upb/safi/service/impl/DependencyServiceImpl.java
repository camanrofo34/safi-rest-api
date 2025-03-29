package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.DependencyController;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DependencyServiceImpl implements DependencyService {

    private final DependencyRepository dependencyRepository;
    private final ComponentRepository componentRepository;
    private final SearchNormalizerUtil searchNormalizerUtil;
    private PagedResourcesAssembler<DependencyResponse> pagedResourcesAssembler;

    @Autowired
    public DependencyServiceImpl(DependencyRepository dependencyRepository,
                                 ComponentRepository componentRepository,
                                 SearchNormalizerUtil searchNormalizerUtil) {
        this.dependencyRepository = dependencyRepository;
        this.componentRepository = componentRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Autowired
    public void setPagedResourcesAssembler(PagedResourcesAssembler<DependencyResponse> pagedResourcesAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Transactional
    @Override
    public EntityModel<DependencyResponse> createDependency(DependencyRequest dependencyRequest) {
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
    public EntityModel<DependencyResponse> updateDependency(Long dependencyId, DependencyRequest dependencyRequest) {
        Dependency dependency = getDependencyByIdOrThrow(dependencyId);
        Component component = getComponentByIdOrThrow(dependencyRequest.getComponentId());
        dependency.setDependencyName(dependencyRequest.getDependencyName());
        dependency.setDependencyDescription(dependencyRequest.getDependencyDescription());
        dependency.setComponent(component);
        dependency = dependencyRepository.save(dependency);
        return mapToResponse(dependency);
    }

    @Override
    public EntityModel<DependencyResponse> getDependencyById(Long dependencyId) {
        Dependency dependency = getDependencyByIdOrThrow(dependencyId);
        return mapToResponse(dependency);
    }

    @Override
    public PagedModel<EntityModel<DependencyResponse>> getDependenciesByComponentId(Long componentId, Pageable pageable) {
        if (!componentRepository.existsById(componentId)) {
            throw new ComponentNotFoundException(componentId.toString());
        }
        Page<DependencyResponse> dependencyResponses = dependencyRepository.findByComponent_ComponentId(componentId, pageable)
                .map(
                        dependency ->
                            new DependencyResponse(
                                    dependency.getDependencyId(),
                                    dependency.getDependencyName(),
                                    dependency.getComponent().getComponentName(),
                                    dependency.getDependencyDescription()
                            )
                );
        return pagedResourcesAssembler.toModel(dependencyResponses, this::mapToEntityModelToResourceModel);
    }

    @Override
    public PagedModel<EntityModel<DependencyResponse>> getDependenciesByDependencyName(String name, Pageable pageable) {
        Page<DependencyResponse> dependencyResponses = dependencyRepository.findAllByDependencyNameContainingIgnoreCase(
                searchNormalizerUtil.normalize(name), pageable)
                .map(
                        dependency ->
                                new DependencyResponse(
                                        dependency.getDependencyId(),
                                        dependency.getDependencyName(),
                                        dependency.getComponent().getComponentName(),
                                        dependency.getDependencyDescription()
                                )
                );
        return pagedResourcesAssembler.toModel(dependencyResponses, this::mapToEntityModelToResourceModel);
    }

    private EntityModel<DependencyResponse> mapToResponse(Dependency dependency) {
        DependencyResponse dependencyResponse = new DependencyResponse(
                dependency.getDependencyId(),
                dependency.getDependencyName(),
                dependency.getComponent().getComponentName(),
                dependency.getDependencyDescription()
        );
        return mapToEntityModel(dependencyResponse);
    }

    private EntityModel<DependencyResponse> mapToEntityModel(DependencyResponse dependencyResponse) {
        return EntityModel.of(dependencyResponse,
                linkTo(methodOn(DependencyController.class).getDependency(dependencyResponse.getDependencyId())).withSelfRel(),
                linkTo(methodOn(DependencyController.class).deleteDependency(dependencyResponse.getDependencyId())).withRel("delete-dependency")
        );
    }

    private EntityModel<DependencyResponse> mapToEntityModelToResourceModel(DependencyResponse dependencyResponse) {
        return EntityModel.of(dependencyResponse,
                linkTo(methodOn(DependencyController.class).getDependency(dependencyResponse.getDependencyId())).withSelfRel()
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