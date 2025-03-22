package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.ComponentRequest;
import unv.upb.safi.domain.dto.response.ComponentResponse;
import unv.upb.safi.domain.entity.Component;
import unv.upb.safi.exception.entityNotFoundException.ComponentNotFoundException;
import unv.upb.safi.repository.ComponentRepository;
import unv.upb.safi.service.ComponentService;

@Service
public class ComponentServiceImpl implements ComponentService {

    private final ComponentRepository componentRepository;
    private final Logger logger = LoggerFactory.getLogger(ComponentServiceImpl.class);

    @Autowired
    public ComponentServiceImpl(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Transactional
    @Override
    public ComponentResponse createComponent(ComponentRequest componentRequest) {
        logger.info("Transaction ID: {}, Adding component {}",
                MDC.get("transactionId"), componentRequest.getComponentName());

        Component component = new Component();
        component.setComponentName(componentRequest.getComponentName());
        component.setComponentDescription(componentRequest.getComponentDescription());
        component = componentRepository.save(component);

        logger.info("Transaction ID: {}, Component added", MDC.get("transactionId"));
        return mapToResponse(component);
    }

    @Transactional
    @Override
    public void deleteComponent(Long id) {
        logger.info("Transaction ID: {}, Deleting component with id {}",
                MDC.get("transactionId"), id);

        Component component = getComponentByIdOrThrow(id);

        if (!component.getDependencies().isEmpty()) {
            throw new DataIntegrityViolationException("Cannot delete component with associated dependencies.");
        }

        logger.info("Transaction ID: {}, Deleting component {}", MDC.get("transactionId"), id);
        componentRepository.deleteById(id);
    }

    @Override
    public ComponentResponse updateComponent(Long componentId, ComponentRequest componentRequest) {
        logger.info("Transaction ID: {}, Updating component with id {}",
                MDC.get("transactionId"), componentId);

        Component component = getComponentByIdOrThrow(componentId);

        component.setComponentName(componentRequest.getComponentName());
        component.setComponentDescription(componentRequest.getComponentDescription());

        component = componentRepository.save(component);

        logger.info("Transaction ID: {}, Component updated", MDC.get("transactionId"));
        return mapToResponse(component);
    }

    @Override
    public ComponentResponse getComponent(Long id) {
        logger.info("Transaction ID: {}, Getting component with id {}",
                MDC.get("transactionId"), id);

        Component component = getComponentByIdOrThrow(id);

        logger.info("Transaction ID: {}, Component found", MDC.get("transactionId"));
        return mapToResponse(component);
    }

    @Override
    public Page<ComponentResponse> getComponents(int page, int size, String sortBy, Sort.Direction direction) {
        logger.info("Transaction ID: {}, Getting all components", MDC.get("transactionId"));

        return componentRepository.findAll(
                PageRequest.of(page, size,
                        Sort.by(direction,
                                sortBy)))
                .map(this::mapToResponse);
    }

    @Override
    public Page<ComponentResponse> getComponentsByName(String name, int page, int size, String sortBy, Sort.Direction direction) {
        logger.info("Transaction ID: {}, Searching components by name '{}'", MDC.get("transactionId"), name);

        return componentRepository.findByComponentNameIgnoreCase(
                name,
                PageRequest.of(page, size,
                        Sort.by(direction,
                                sortBy)))
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

