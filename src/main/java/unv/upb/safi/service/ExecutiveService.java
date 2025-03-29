package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import unv.upb.safi.domain.dto.request.ExecutiveRequest;
import unv.upb.safi.domain.dto.response.ExecutiveResponse;

public interface ExecutiveService {
    @Transactional
    EntityModel<ExecutiveResponse> createExecutive(ExecutiveRequest executiveRequest);

    EntityModel<ExecutiveResponse> updateExecutive(Long executiveId, ExecutiveRequest executiveRequest);

    @Transactional
    void deleteExecutive(Long id);

    EntityModel<ExecutiveResponse> getExecutive(Long id);

    PagedModel<EntityModel<ExecutiveResponse>> getExecutives(Pageable pageable);

    PagedModel<EntityModel<ExecutiveResponse>> getExecutivesByExecutiveName(String name, Pageable pageable);
}
