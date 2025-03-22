package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import unv.upb.safi.domain.dto.request.ExecutiveRequest;
import unv.upb.safi.domain.dto.response.ExecutiveResponse;

public interface ExecutiveService {
    @Transactional
    ExecutiveResponse createExecutive(ExecutiveRequest executiveRequest);

    ExecutiveResponse updateExecutive(Long executiveId, ExecutiveRequest executiveRequest);

    @Transactional
    void deleteExecutive(Long id);

    ExecutiveResponse getExecutive(Long id);

    Page<ExecutiveResponse> getExecutives(int page, int size, String sortBy, Sort.Direction direction);
}
