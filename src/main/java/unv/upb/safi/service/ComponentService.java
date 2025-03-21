package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.ComponentRequest;
import unv.upb.safi.domain.dto.response.ComponentResponse;
import unv.upb.safi.domain.entity.Component;
import unv.upb.safi.exception.entityNotFoundException.ComponentNotFoundException;
import unv.upb.safi.repository.ComponentRepository;

import java.util.List;

@Service
public class ComponentService {

    private final ComponentRepository componentRepository;
    private final Logger logger = LoggerFactory.getLogger(ComponentService.class);

    @Autowired
    public ComponentService(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    public ComponentResponse createComponent(ComponentRequest componentRequest) {
        logger.info("Transaction ID: {}, Adding component {}",
                MDC.get("transactionId"), componentRequest.getComponentName());

        Component component = new Component();
        component.setComponentName(componentRequest.getComponentName());
        component = componentRepository.save(component);

        logger.info("Transaction ID: {}, Component added", MDC.get("transactionId"));
        return mapToResponse(component);
    }

    @Transactional
    public void deleteComponent(Long id) {
        logger.info("Transaction ID: {}, Deleting component with id {}",
                MDC.get("transactionId"), id);

        if (!componentRepository.existsById(id)) {
            throw new ComponentNotFoundException(id.toString());
        }

        logger.info("Transaction ID: {}, Deleting component {}", MDC.get("transactionId"), id);
        componentRepository.deleteById(id);
    }

    public ComponentResponse updateComponent(Long componentId, ComponentRequest componentRequest) {
        logger.info("Transaction ID: {}, Updating component with id {}",
                MDC.get("transactionId"), componentId);

        Component component = componentRepository.findById(componentId)
                .orElseThrow(() -> new ComponentNotFoundException(componentId.toString()));

        component.setComponentName(componentRequest.getComponentName());
        component = componentRepository.save(component);

        logger.info("Transaction ID: {}, Component updated", MDC.get("transactionId"));
        return mapToResponse(component);
    }

    public ComponentResponse getComponent(Long id) {
        logger.info("Transaction ID: {}, Getting component with id {}",
                MDC.get("transactionId"), id);

        Component component = componentRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException(id.toString()));

        logger.info("Transaction ID: {}, Component found", MDC.get("transactionId"));
        return mapToResponse(component);
    }

    public List<ComponentResponse> getComponents() {
        logger.info("Transaction ID: {}, Getting all components", MDC.get("transactionId"));

        return componentRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ComponentResponse> getComponentsByName(String name) {
        logger.info("Transaction ID: {}, Searching components by name '{}'", MDC.get("transactionId"), name);

        return componentRepository.findByComponentNameIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ComponentResponse mapToResponse(Component component) {
        return new ComponentResponse(
                component.getComponentId(),
                component.getComponentName()
        );
    }
}

