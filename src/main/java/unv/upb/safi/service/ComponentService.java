package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import unv.upb.safi.domain.dto.request.ComponentRequest;
import unv.upb.safi.domain.dto.response.ComponentResponse;

public interface ComponentService {
    @Transactional
    ComponentResponse createComponent(ComponentRequest componentRequest);

    @Transactional
    void deleteComponent(Long id);

    ComponentResponse updateComponent(Long componentId, ComponentRequest componentRequest);

    ComponentResponse getComponent(Long id);

    Page<ComponentResponse> getComponents(int page, int size, String sortBy, Sort.Direction direction);

    Page<ComponentResponse> getComponentsByName(String name, int page, int size, String sortBy, Sort.Direction direction);
}
