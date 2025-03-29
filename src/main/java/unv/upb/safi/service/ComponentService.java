package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import unv.upb.safi.domain.dto.request.ComponentRequest;
import unv.upb.safi.domain.dto.response.ComponentResponse;

public interface ComponentService {
    @Transactional
    EntityModel<ComponentResponse> createComponent(ComponentRequest componentRequest);

    @Transactional
    void deleteComponent(Long id);

    EntityModel<ComponentResponse> updateComponent(Long componentId, ComponentRequest componentRequest);

    EntityModel<ComponentResponse> getComponent(Long id);

    PagedModel<EntityModel<ComponentResponse>> getComponents(Pageable pageable);

    PagedModel<EntityModel<ComponentResponse>> getComponentsByName(String name, Pageable pageable);
}
