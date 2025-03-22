package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import unv.upb.safi.domain.dto.request.PqrsfRequest;
import unv.upb.safi.domain.dto.response.PqrsfResponse;

public interface PqrsfService {
    @Transactional
    PqrsfResponse createPqrsf(Long studentId, PqrsfRequest pqrsfRequest);

    @Transactional
    void deletePqrsf(Long requestId);

    PqrsfResponse getPqrsf(Long requestId);

    Page<PqrsfResponse> getAllPqrsf(int page, int size, String sortBy, Sort.Direction direction);
}
