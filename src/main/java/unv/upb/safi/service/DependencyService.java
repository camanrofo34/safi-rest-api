package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import unv.upb.safi.domain.dto.request.DependencyRequest;
import unv.upb.safi.domain.dto.response.DependencyResponse;

public interface DependencyService {
    @Transactional
    EntityModel<DependencyResponse> createDependency(DependencyRequest dependencyRequest);

    @Transactional
    void deleteDependency(Long id);

    EntityModel<DependencyResponse> updateDependency(Long dependencyId, DependencyRequest dependencyRequest);

    PagedModel<EntityModel<DependencyResponse>> getDependenciesByComponentId(Long componentId, Pageable pageable);

    PagedModel<EntityModel<DependencyResponse>> getDependenciesByDependencyName(String dependencyName ,Pageable pageable);

    EntityModel<DependencyResponse> getDependencyById(Long dependencyId);

}
