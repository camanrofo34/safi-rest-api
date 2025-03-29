package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import unv.upb.safi.domain.dto.request.CollegeRequest;
import unv.upb.safi.domain.dto.response.CollegeResponse;

public interface CollegeService {
    @Transactional
    EntityModel<CollegeResponse> addCollege(CollegeRequest collegeRequest);

    @Transactional
    void deleteCollege(Long id);

    EntityModel<CollegeResponse> getCollege(Long id);

    PagedModel<EntityModel<CollegeResponse>> getColleges(Pageable pageable);

    PagedModel<EntityModel<CollegeResponse>> getCollegesByName(String name, Pageable pageable);
}
