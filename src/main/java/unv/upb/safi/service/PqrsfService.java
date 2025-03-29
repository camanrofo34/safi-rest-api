package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import unv.upb.safi.domain.dto.request.PqrsfRequest;
import unv.upb.safi.domain.dto.response.PqrsfResponse;

public interface PqrsfService {
    @Transactional
    EntityModel<PqrsfResponse> createPqrsf(Long studentId, PqrsfRequest pqrsfRequest);

    @Transactional
    void deletePqrsf(Long requestId);

    EntityModel<PqrsfResponse> getPqrsf(Long requestId);

    PagedModel<EntityModel<PqrsfResponse>> getAllPqrsf(Pageable pageable);
}
