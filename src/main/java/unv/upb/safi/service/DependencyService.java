package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import unv.upb.safi.domain.dto.request.DependencyRequest;
import unv.upb.safi.domain.dto.response.DependencyResponse;

public interface DependencyService {
    @Transactional
    DependencyResponse createDependency(DependencyRequest dependencyRequest);

    @Transactional
    void deleteDependency(Long id);

    DependencyResponse updateDependency(Long dependencyId, DependencyRequest dependencyRequest);

    Page<DependencyResponse> getDependenciesByComponentId(Long componentId, Pageable pageable);

    Page<DependencyResponse> getDependenciesByDependencyName(String dependencyName ,Pageable pageable);

    DependencyResponse getDependencyById(Long dependencyId);

}
