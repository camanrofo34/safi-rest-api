package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import unv.upb.safi.domain.dto.request.CollegeRequest;
import unv.upb.safi.domain.dto.response.CollegeResponse;

public interface CollegeService {
    @Transactional
    CollegeResponse addCollege(CollegeRequest collegeRequest);

    @Transactional
    void deleteCollege(Long id);

    CollegeResponse getCollege(Long id);

    Page<CollegeResponse> getColleges(int page, int size, String sortBy, Sort.Direction direction);

    Page<CollegeResponse> getCollegesByName(String name, int page, int size, String sortBy, Sort.Direction direction);
}
