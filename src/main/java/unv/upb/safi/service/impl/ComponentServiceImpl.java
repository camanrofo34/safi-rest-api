package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.ComponentRequest;
import unv.upb.safi.domain.dto.response.ComponentResponse;
import unv.upb.safi.domain.entity.Component;
import unv.upb.safi.exception.entityNotFoundException.ComponentNotFoundException;
import unv.upb.safi.repository.ComponentRepository;
import unv.upb.safi.service.ComponentService;
import unv.upb.safi.util.SearchNormalizerUtil;

@Service
public class ComponentServiceImpl implements ComponentService {

    private final ComponentRepository componentRepository;

    private final SearchNormalizerUtil searchNormalizerUtil;

    @Autowired
    public ComponentServiceImpl(ComponentRepository componentRepository, SearchNormalizerUtil searchNormalizerUtil) {
        this.componentRepository = componentRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Transactional
    @Override
    public ComponentResponse createComponent(ComponentRequest componentRequest) {
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
    public ComponentResponse updateComponent(Long componentId, ComponentRequest componentRequest) {
        Component component = getComponentByIdOrThrow(componentId);

        component.setComponentName(componentRequest.getComponentName());
        component.setComponentDescription(componentRequest.getComponentDescription());

        component = componentRepository.save(component);

        return mapToResponse(component);
    }

    @Override
    public ComponentResponse getComponent(Long id) {
        Component component = getComponentByIdOrThrow(id);


        return mapToResponse(component);
    }

    @Override
    public Page<ComponentResponse> getComponents(Pageable pageable) {
        return componentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<ComponentResponse> getComponentsByName(String name, Pageable pageable) {
        return componentRepository.findByComponentNameContainingIgnoreCase(
                        searchNormalizerUtil.normalize(name), pageable)
                .map(this::mapToResponse);
    }

    private ComponentResponse mapToResponse(Component component) {
        return new ComponentResponse(
                component.getComponentId(),
                component.getComponentName(),
                component.getComponentDescription()
        );
    }

    private Component getComponentByIdOrThrow(Long componentId) {
        return componentRepository.findById(componentId)
                .orElseThrow(() -> new ComponentNotFoundException(componentId.toString()));
    }
}

