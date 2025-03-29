package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.ComponentController;
import unv.upb.safi.domain.dto.request.ComponentRequest;
import unv.upb.safi.domain.dto.response.ComponentResponse;
import unv.upb.safi.domain.entity.Component;
import unv.upb.safi.exception.entityNotFoundException.ComponentNotFoundException;
import unv.upb.safi.repository.ComponentRepository;
import unv.upb.safi.service.ComponentService;
import unv.upb.safi.util.SearchNormalizerUtil;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ComponentServiceImpl implements ComponentService {

    private final ComponentRepository componentRepository;

    private final SearchNormalizerUtil searchNormalizerUtil;

    private PagedResourcesAssembler<ComponentResponse> pagedResourcesAssembler;

    @Autowired
    public ComponentServiceImpl(ComponentRepository componentRepository, SearchNormalizerUtil searchNormalizerUtil) {
        this.componentRepository = componentRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Autowired
    public void setPagedResourcesAssembler(PagedResourcesAssembler<ComponentResponse> pagedResourcesAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Transactional
    @Override
    public EntityModel<ComponentResponse> createComponent(ComponentRequest componentRequest) {
        Component component = new Component();
        component.setComponentName(componentRequest.getComponentName());
        component.setComponentDescription(componentRequest.getComponentDescription());
        component = componentRepository.save(component);

        return mapToResponse(component);
    }

    @Transactional
    @Override
    public void deleteComponent(Long id) {
        Component component = getComponentByIdOrThrow(id);

        if (!component.getDependencies().isEmpty()) {
            throw new DataIntegrityViolationException("Cannot delete component with associated dependencies.");
        }

        componentRepository.deleteById(id);
    }

    @Override
    public EntityModel<ComponentResponse> updateComponent(Long componentId, ComponentRequest componentRequest) {
        Component component = getComponentByIdOrThrow(componentId);

        component.setComponentName(componentRequest.getComponentName());
        component.setComponentDescription(componentRequest.getComponentDescription());

        component = componentRepository.save(component);

        return mapToResponse(component);
    }

    @Override
    public EntityModel<ComponentResponse> getComponent(Long id) {
        Component component = getComponentByIdOrThrow(id);


        return mapToResponse(component);
    }

    @Override
    public PagedModel<EntityModel<ComponentResponse>> getComponents(Pageable pageable) {
        Page<ComponentResponse> componentResponses = componentRepository.findAll(pageable)
                .map(component ->
                        new ComponentResponse(
                                component.getComponentId(),
                                component.getComponentName(),
                                component.getComponentDescription()
                        )
                );

        return pagedResourcesAssembler.toModel(componentResponses, this::mapToEntityModelToPagedResources);
    }

    @Override
    public PagedModel<EntityModel<ComponentResponse>> getComponentsByName(String name, Pageable pageable) {
        Page<ComponentResponse> componentResponses = componentRepository.findAllByComponentNameContainingIgnoreCase(
                searchNormalizerUtil.normalize(name), pageable)
                .map(component -> new ComponentResponse(
                        component.getComponentId(),
                        component.getComponentName(),
                        component.getComponentDescription()
                ));

        return pagedResourcesAssembler.toModel(componentResponses, this::mapToEntityModelToPagedResources);
    }

    private EntityModel<ComponentResponse> mapToResponse(Component component) {
        ComponentResponse componentResponse = new ComponentResponse(
                component.getComponentId(),
                component.getComponentName(),
                component.getComponentDescription()
        );

        return mapToEntityModel(componentResponse);

    }

    private EntityModel<ComponentResponse> mapToEntityModel(ComponentResponse componentResponse) {
        return EntityModel.of(componentResponse,
                linkTo(methodOn(ComponentController.class).getComponent(componentResponse.getComponentId())).withSelfRel(),
                linkTo(methodOn(ComponentController.class).deleteComponent(componentResponse.getComponentId())).withRel("delete-component")
        );
    }

    private EntityModel<ComponentResponse> mapToEntityModelToPagedResources(ComponentResponse componentResponse) {
        return EntityModel.of(componentResponse,
                linkTo(methodOn(ComponentController.class).getComponent(componentResponse.getComponentId())).withSelfRel()
        );
    }

    private Component getComponentByIdOrThrow(Long componentId) {
        return componentRepository.findById(componentId)
                .orElseThrow(() -> new ComponentNotFoundException(componentId.toString()));
    }
}

